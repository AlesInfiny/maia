package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.consumer.controller.dto.catalog.CatalogCategoryResponse;
import com.dressca.web.consumer.mapper.CatalogCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * {@link CatalogCategory} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(
    name = "CatalogCategories",
    description = "カタログカテゴリの情報にアクセスする API です。")
@RequestMapping("/api/catalog-categories")
@AllArgsConstructor
public class CatalogCategoriesController {

  @Autowired
  private CatalogApplicationService service;

  /**
   * カタログカテゴリの一覧を取得します。
   * 
   * @return カタログカテゴリの一覧。
   */
  @Operation(
      summary = "カタログカテゴリの一覧を取得します。",
      description = "カタログカテゴリの一覧を取得します。")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "成功。",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = CatalogCategoryResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<CatalogCategoryResponse>> getCatalogCategories() {
    List<CatalogCategoryResponse> categories = this.service.getCategories().stream()
        .map(CatalogCategoryMapper::convert)
        .collect(Collectors.toList());

    return ResponseEntity.ok().body(categories);
  }
}
