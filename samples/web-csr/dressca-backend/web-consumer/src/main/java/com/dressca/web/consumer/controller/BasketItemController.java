package com.dressca.web.consumer.controller;

import com.dressca.applicationcore.applicationservice.BasketDetail;
import com.dressca.applicationcore.applicationservice.ShoppingApplicationService;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.CatalogItemInBasketNotFoundException;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.web.controller.advice.ProblemDetailsFactory;
import com.dressca.web.constant.WebConstants;
import com.dressca.web.consumer.controller.dto.baskets.BasketItemResponse;
import com.dressca.web.consumer.controller.dto.baskets.BasketResponse;
import com.dressca.web.consumer.controller.dto.baskets.PostBasketItemsRequest;
import com.dressca.web.consumer.controller.dto.baskets.PutBasketItemsRequest;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryResponse;
import com.dressca.web.log.ErrorMessageBuilder;
import com.dressca.web.consumer.mapper.BasketMapper;
import com.dressca.web.consumer.mapper.CatalogItemSummaryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link BasketItem} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "BasketItems", description = "買い物かごアイテムの情報にアクセスする API です。")
@RequestMapping("/api/basket-items")
@AllArgsConstructor
public class BasketItemController {

  @Autowired
  private ShoppingApplicationService shoppingApplicationService;

  @Autowired
  private ProblemDetailsFactory problemDetailsFactory;

  /**
   * 買い物かごアイテムの一覧を取得します。
   * 
   * @return 買い物かごアイテムの一覧。
   */
  @Operation(summary = "買い物かごアイテムの一覧を取得します。", description = "買い物かごアイテムの一覧を返却します。")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功。",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = BasketResponse.class)))})
  @GetMapping
  public ResponseEntity<BasketResponse> getBasketItems(HttpServletRequest req) {
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();
    BasketDetail basketItemsForUser = shoppingApplicationService.getBasketDetail(buyerId);
    Basket basket = basketItemsForUser.getBasket();
    List<CatalogItem> catalogItems = basketItemsForUser.getCatalogItems();
    BasketResponse basketDto = BasketMapper.convert(basket);

    for (BasketItemResponse item : basketDto.getBasketItems()) {
      item.setCatalogItem(this.getCatalogItemResponse(item.getCatalogItemId(), catalogItems));
    }
    List<Long> deletedItemIds = basketItemsForUser.getDeletedItemIds();
    basketDto.setDeletedItemIds(deletedItemIds);
    return ResponseEntity.ok().body(basketDto);
  }

  /**
   * 買い物かごアイテム内の数量を変更します。買い物かご内に存在しないカタログアイテム ID は指定できません。
   * 
   * <p>この API では、買い物かご内に存在する商品の数量を変更できます。
   * 買い物かご内に存在しないカタログアイテム ID を指定すると HTTP 400 を返却します。
   * またシステムに登録されていないカタログアイテム ID を指定した場合も HTTP 400 を返却します。</p>
   * 
   * @param putBasketItems 変更する買い物かごアイテムのデータリスト。
   * @return なし。
   */
  @Operation(summary = "買い物かごアイテム内の数量を変更します。",
      description = "買い物かごアイテム内の数量を変更します。買い物かご内に存在しないカタログアイテム ID は指定できません。<br>"
          + "この API では、買い物かご内に存在する商品の数量を変更できます。買い物かご内に存在しないカタログアイテム ID を指定すると HTTP 400 を返却します。<br>"
          + "またシステムに登録されていないカタログアイテム ID を指定した場合も HTTP 400 を返却します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "成功。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class)))})
  @PutMapping()
  public ResponseEntity<?> putBasketItems(@RequestBody List<PutBasketItemsRequest> putBasketItems,
      HttpServletRequest req) {
    if (putBasketItems.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    Map<Long, Integer> quantities = putBasketItems.stream().collect(Collectors
        .toMap(PutBasketItemsRequest::getCatalogItemId, PutBasketItemsRequest::getQuantity));
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();

    try {
      shoppingApplicationService.setQuantities(buyerId, quantities);
    } catch (CatalogNotFoundException e) {
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS, HttpStatus.BAD_REQUEST);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    } catch (CatalogItemInBasketNotFoundException e) {
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS, HttpStatus.BAD_REQUEST);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * 買い物かごに商品を追加します。
   * 
   * <p>この API では、システムに登録されていないカタログアイテム ID を指定した場合 HTTP 400 を返却します。
   * また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します。
   * すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します。</p>
   * 
   * <p>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません。 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します。</p>
   * 
   * @param postBasketItem 追加する商品の情報。
   * @return なし。
   */
  @Operation(summary = "買い物かごに商品を追加します。",
      description = "買い物かごに商品を追加します。<br>"
          + "この API では、システムに登録されていないカタログアイテム ID を指定した場合 HTTP 400 を返却します。"
          + "また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します。"
          + "すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します。<br>"
          + "買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません。計算の結果数量が 0 未満になる場合 HTTP 500 を返却します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "作成完了。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))),
          @ApiResponse(responseCode = "500", description = "サーバーエラー。",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class)))})
  @PostMapping
  public ResponseEntity<?> postBasketItem(@RequestBody PostBasketItemsRequest postBasketItem,
      HttpServletRequest req) {
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();
    try {
      this.shoppingApplicationService.addItemToBasket(buyerId, postBasketItem.getCatalogItemId(),
          postBasketItem.getAddedQuantity());
    } catch (CatalogNotFoundException e) {
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS, HttpStatus.BAD_REQUEST);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    }
    return ResponseEntity.created(URI.create("/basket-items")).build();
  }

  /**
   * 買い物かごから指定したカタログアイテム ID の商品を削除します。
   * 
   * <p>catalogItemId には買い物かご内に存在するカタログアイテム ID を指定してください。
   * カタログアイテム ID は 1 以上の整数です。
   * 0 以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します。
   * 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します。</p>
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @return なし。
   */
  @Operation(summary = "買い物かごから指定したカタログアイテム ID の商品を削除します。",
      description = "買い物かごから指定したカタログアイテム ID の商品を削除します。<br>"
          + "catalogItemId には買い物かご内に存在するカタログアイテム ID を指定してください。カタログアイテム ID は 1 以上の整数です。"
          + "0 以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します。"
          + "買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "成功。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))),
          @ApiResponse(responseCode = "404", description = "買い物かご内に指定したカタログアイテム ID がありません。",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class)))})
  @DeleteMapping("{catalogItemId}")
  public ResponseEntity<?> deleteBasketItem(@PathVariable("catalogItemId") long catalogItemId,
      HttpServletRequest req) {
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();

    try {
      this.shoppingApplicationService.deleteItemFromBasket(buyerId, catalogItemId);
    } catch (CatalogNotFoundException e) {
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS, HttpStatus.BAD_REQUEST);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    } catch (CatalogItemInBasketNotFoundException e) {
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS, HttpStatus.NOT_FOUND);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    }
    return ResponseEntity.noContent().build();
  }

  private CatalogItemSummaryResponse getCatalogItemResponse(long catalogItemId,
      List<CatalogItem> catalogItems) {
    CatalogItem catalogItem = catalogItems.stream().filter(item -> item.getId() == catalogItemId)
        .findFirst().orElse(null);

    return convertCatalogItemDto(catalogItem);
  }

  private CatalogItemSummaryResponse convertCatalogItemDto(CatalogItem catalogItem) {
    if (catalogItem == null) {
      return null;
    }

    return CatalogItemSummaryMapper.convert(catalogItem);
  }
}
