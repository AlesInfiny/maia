package com.dressca.web.admin.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogManagementApplicationService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemUpdateCommand;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.web.admin.controller.dto.catalog.CatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PagedListOfCatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PostCatalogItemRequest;
import com.dressca.web.admin.controller.dto.catalog.PutCatalogItemRequest;
import com.dressca.web.admin.mapper.CatalogItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "CatalogItems", description = "カタログアイテムの情報にアクセスする API コントローラーです.")
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
public class CatalogItemsController {

  @Autowired
  private CatalogManagementApplicationService service;

  /**
   * 指定したIDのカタログアイテムを返します。
   * 
   * @param id ID。
   * @return カタログアイテム。
   */
  @Operation(summary = "指定したIDのカタログアイテムを返します。", description = "指定したIDのカタログアイテムを返します。")
  @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedListOfCatalogItemResponse.class)))
  @GetMapping("{id}")
  public ResponseEntity<CatalogItemResponse> getById(@PathVariable("id") long id) {
    CatalogItem item;
    try {
      item = this.service.GetCatalogItem(id);
    } catch (CatalogNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
    CatalogItemResponse returnValue = CatalogItemMapper.convert(item);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログアイテムを検索して返します。
   *
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ番号。未指定の場合は1。
   * @param pageSize   ページサイズ。未指定の場合は20。
   * @return カタログアイテムの一覧
   */
  @Operation(summary = "カタログアイテムを検索して返します.", description = "カタログアイテムを検索して返します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedListOfCatalogItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "リクエストエラー", content = @Content) })
  @GetMapping()
  public ResponseEntity<PagedListOfCatalogItemResponse> getByQuery(
      @RequestParam(name = "brandId", defaultValue = "0") long brandId,
      @RequestParam(name = "categoryId", defaultValue = "0") long categoryId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
    List<CatalogItemResponse> items = this.service.getCatalogItems(brandId,
        categoryId, page, pageSize).stream()
        .map(CatalogItemMapper::convert)
        .collect(Collectors.toList());
    int totalCount = this.service.countCatalogItems(brandId, categoryId);

    PagedListOfCatalogItemResponse returnValue = new PagedListOfCatalogItemResponse(
        items,
        totalCount,
        page,
        pageSize);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログにアイテムを追加します。
   * 
   * @param postCatalogItemRequest 追加するカタログアイテム
   * @return 追加したカタログアイテム
   */
  @Operation(summary = "カタログにアイテムを追加します。", description = "カタログにアイテムを追加します。")
  @ApiResponse(responseCode = "201", description = "成功。", content = @Content)
  @PostMapping
  public ResponseEntity<CatalogItem> postCatalogItem(@RequestBody PostCatalogItemRequest postCatalogItemRequest) {
    this.service.addItemToCatalog(
        postCatalogItemRequest.getName(),
        postCatalogItemRequest.getDescription(),
        new BigDecimal(postCatalogItemRequest.getPrice()),
        postCatalogItemRequest.getProductCode(),
        postCatalogItemRequest.getCatalogBrandId(),
        postCatalogItemRequest.getCatalogCategoryId());

    return ResponseEntity.created(URI.create("catalog-items")).build();
  }

  /**
   * カタログから指定したカタログアイテム ID のアイテムを削除します。
   * 
   * @param catalogItemId カタログアイテムID。
   * @return なし。
   */
  @Operation(summary = "カタログから指定したカタログアイテム ID のアイテムを削除します。", description = "カタログから指定したカタログアイテム ID のアイテムを削除します。")
  @ApiResponse(responseCode = "204", description = "成功.", content = @Content)
  @DeleteMapping("{catalogItemId}")
  public ResponseEntity<CatalogItem> deleteCatalogItem(@PathVariable("catalogItemId") long catalogItemId) {

    try {
      this.service.deleteItemFromCatalog(catalogItemId);
    } catch (CatalogNotFoundException e) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }

  /**
   * 指定したIDのカタログアイテムの情報を更新します。
   * 
   * @param catalogItemId         カタログアイテムID。
   * @param putCatalogItemRequest 更新するカタログアイテムの情報。
   * @return なし。
   */
  @Operation(summary = "指定したIDのカタログアイテムの情報を更新します。", description = "指定したIDのカタログアイテムの情報を更新します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "成功.", content = @Content),
      @ApiResponse(responseCode = "401", description = "認可エラー", content = @Content),
      @ApiResponse(responseCode = "404", description = "対象のIDが存在しない。", content = @Content),
      @ApiResponse(responseCode = "409", description = "更新の競合が発生。", content = @Content),
  })
  @PutMapping("{catalogItemId}")
  public ResponseEntity<CatalogItem> putCatalogItem(@PathVariable("catalogItemId") long catalogItemId,
      @RequestBody PutCatalogItemRequest putCatalogItemRequest) {

    CatalogItemUpdateCommand command = new CatalogItemUpdateCommand(
        catalogItemId,
        putCatalogItemRequest.getName(),
        putCatalogItemRequest.getDescription(),
        new BigDecimal(putCatalogItemRequest.getPrice()),
        putCatalogItemRequest.getProductCode(),
        putCatalogItemRequest.getCatalogBrandId(),
        putCatalogItemRequest.getCatalogCategoryId(),
        putCatalogItemRequest.getRowVersion());

    try {
      this.service.updateCatalogItem(command);
    } catch (CatalogNotFoundException e) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }
}
