package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemResponse;
import com.dressca.web.consumer.controller.dto.catalog.PagedListOfCatalogItemResponse;
import com.dressca.web.consumer.mapper.CatalogItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
  private CatalogApplicationService service;

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
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
    List<CatalogItemResponse> items = service.getCatalogItemsByConsumer(brandId, categoryId, page, pageSize).stream()
        .map(CatalogItemMapper::convert)
        .collect(Collectors.toList());
    int totalCount = service.countCatalogItems(brandId, categoryId);

    PagedListOfCatalogItemResponse returnValue = new PagedListOfCatalogItemResponse(
        items,
        totalCount,
        page,
        pageSize);
    return ResponseEntity.ok().body(returnValue);
  }
}
