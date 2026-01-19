package com.dressca.web.admin.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogApplicationService;
import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.OptimisticLockingFailureException;
import com.dressca.applicationcore.constant.UserRoleConstants;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import com.dressca.web.admin.controller.dto.catalog.GetCatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PagedListOfGetCatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PostCatalogItemRequest;
import com.dressca.web.admin.controller.dto.catalog.PutCatalogItemRequest;
import com.dressca.web.admin.mapper.CatalogItemMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * {@link CatalogItem} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "CatalogItems", description = "カタログアイテムの情報にアクセスする API です。")
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
@PreAuthorize(value = "hasAuthority('" + UserRoleConstants.ADMIN + "')")
public class CatalogItemsController {

  @Autowired
  private CatalogApplicationService service;

  @Autowired
  private AbstractStructuredLogger apLog;

  /**
   * 指定した ID のカタログアイテムを返します。
   * 
   * @param id ID 。
   * @return カタログアイテム。
   * @throws PermissionDeniedException 認可エラー。
   */
  @Operation(summary = "指定した ID のカタログアイテムを返します。", description = "指定した ID のカタログアイテムを返します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = GetCatalogItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "リクエストエラー。", content = @Content),
      @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
      @ApiResponse(responseCode = "404", description = "指定した ID のアイテムがカタログに存在しません。",
          content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @GetMapping("{id}")
  public ResponseEntity<GetCatalogItemResponse> getCatalogItem(@PathVariable("id") long id)
      throws PermissionDeniedException {
    CatalogItem item;
    try {
      item = this.service.getCatalogItem(id);
    } catch (CatalogNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
    GetCatalogItemResponse returnValue = CatalogItemMapper.convert(item);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログアイテムを検索して返します。
   *
   * @param brandId ブランド ID 。未指定の場合は 0 。
   * @param categoryId カテゴリ ID 。未指定の場合は 0 。
   * @param page ページ番号。未指定の場合は 1 。
   * @param pageSize ページサイズ。未指定の場合は 20 。
   * @return カタログアイテムの一覧。
   * @throws PermissionDeniedException 認可エラー。
   */
  @Operation(summary = "カタログアイテムを検索して返します。", description = "カタログアイテムを検索して返します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PagedListOfGetCatalogItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "リクエストエラー。", content = @Content),
      @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
      @ApiResponse(responseCode = "404", description = "失敗。", content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @GetMapping
  public ResponseEntity<PagedListOfGetCatalogItemResponse> getByQuery(
      @RequestParam(name = "brandId", defaultValue = "0") long brandId,
      @RequestParam(name = "categoryId", defaultValue = "0") long categoryId,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize)
      throws PermissionDeniedException {

    List<GetCatalogItemResponse> items =
        this.service.getCatalogItemsForAdmin(brandId, categoryId, page, pageSize).stream()
            .map(CatalogItemMapper::convert).collect(Collectors.toList());
    int totalCount = this.service.countCatalogItemsForAdmin(brandId, categoryId);

    PagedListOfGetCatalogItemResponse returnValue =
        new PagedListOfGetCatalogItemResponse(items, totalCount, page, pageSize);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログにアイテムを追加します。
   * 
   * @param postCatalogItemRequest 追加するアイテムの情報。
   * @return なし。
   * @throws PermissionDeniedException 認可エラー。
   */
  @Operation(summary = "カタログにアイテムを追加します。", description = "カタログにアイテムを追加します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "成功。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。", content = @Content),
          @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
          @ApiResponse(responseCode = "404", description = "失敗。", content = @Content),
          @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @PostMapping
  public ResponseEntity<CatalogItem> postCatalogItem(
      @RequestBody PostCatalogItemRequest postCatalogItemRequest) throws PermissionDeniedException {

    try {
      CatalogItem addedCatalogItem = this.service.addItemToCatalog(postCatalogItemRequest.getName(),
          postCatalogItemRequest.getDescription(),
          new BigDecimal(postCatalogItemRequest.getPrice()),
          postCatalogItemRequest.getProductCode(), postCatalogItemRequest.getCatalogCategoryId(),
          postCatalogItemRequest.getCatalogBrandId());
      return ResponseEntity.created(URI.create("/api/catalog-items/" + addedCatalogItem.getId()))
          .build();
    } catch (CatalogBrandNotFoundException | CatalogCategoryNotFoundException e) {
      apLog.error(ExceptionUtils.getStackTrace(e));
      // ここでは発生を想定していないので、システムエラーとします。
      throw new SystemException(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    }
  }

  /**
   * カタログから指定したカタログアイテム ID のアイテムを削除します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @param rowVersion 行バージョン。
   * @return なし。
   * @throws PermissionDeniedException 認可エラー。
   * @throws OptimisticLockingFailureException 楽観ロックエラー。
   */
  @Operation(summary = "カタログから指定したカタログアイテム ID のアイテムを削除します。",
      description = "カタログから指定したカタログアイテム ID のアイテムを削除します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "成功。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。", content = @Content),
          @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
          @ApiResponse(responseCode = "404", description = "指定した ID のアイテムがカタログに存在しません。",
              content = @Content),
          @ApiResponse(responseCode = "409", description = "競合が発生。", content = @Content),
          @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @DeleteMapping("{catalogItemId}")
  public ResponseEntity<?> deleteCatalogItem(@PathVariable("catalogItemId") long catalogItemId,
      @RequestParam OffsetDateTime rowVersion)
      throws PermissionDeniedException, OptimisticLockingFailureException {
    try {
      this.service.deleteItemFromCatalog(catalogItemId, rowVersion);
    } catch (CatalogNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定した ID のカタログアイテムの情報を更新します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @param putCatalogItemRequest 更新するカタログアイテムの情報。
   * @return なし。
   * @throws OptimisticLockingFailureException 楽観ロックエラー。
   * @throws PermissionDeniedException 認可エラー。
   */
  @Operation(summary = "指定した ID のカタログアイテムの情報を更新します。", description = "指定した ID のカタログアイテムの情報を更新します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "成功。", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー。", content = @Content),
          @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
          @ApiResponse(responseCode = "404", description = "指定した ID のアイテムがカタログに存在しません。",
              content = @Content),
          @ApiResponse(responseCode = "409", description = "競合が発生。", content = @Content),
          @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @PutMapping("{catalogItemId}")
  public ResponseEntity<?> putCatalogItem(@PathVariable("catalogItemId") long catalogItemId,
      @RequestBody PutCatalogItemRequest putCatalogItemRequest)
      throws PermissionDeniedException, OptimisticLockingFailureException {
    try {
      this.service.updateCatalogItem(catalogItemId, putCatalogItemRequest.getName(),
          putCatalogItemRequest.getDescription(), new BigDecimal(putCatalogItemRequest.getPrice()),
          putCatalogItemRequest.getProductCode(), putCatalogItemRequest.getCatalogCategoryId(),
          putCatalogItemRequest.getCatalogBrandId(), putCatalogItemRequest.getRowVersion(),
          putCatalogItemRequest.getIsDeleted());
    } catch (CatalogNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    } catch (CatalogBrandNotFoundException | CatalogCategoryNotFoundException e) {
      apLog.error(ExceptionUtils.getStackTrace(e));
      // ここでは発生を想定していないので、システムエラーとします。
      throw new SystemException(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    }
    return ResponseEntity.noContent().build();
  }
}
