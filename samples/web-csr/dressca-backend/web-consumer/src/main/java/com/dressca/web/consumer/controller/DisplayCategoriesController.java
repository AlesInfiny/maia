package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.DisplayApplicationService;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayCategoryResponse;
import com.dressca.web.consumer.mapper.DisplayCategoryMapper;
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
import lombok.RequiredArgsConstructor;

/**
 * {@link CatalogCategory} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "DisplayCategories", description = "掲載カテゴリの情報にアクセスする API です。")
@RequestMapping("/api/display-categories")
@RequiredArgsConstructor
public class DisplayCategoriesController {

  private final DisplayApplicationService service;

  /**
   * 掲載カテゴリの一覧を取得します。
   * 
   * @return 掲載カテゴリの一覧。
   */
  @Operation(summary = "掲載カテゴリの一覧を取得します。", description = "掲載カテゴリの一覧を取得します。")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功。",
      content = @Content(mediaType = "application/json",
          array = @ArraySchema(
              schema = @Schema(implementation = GetDisplayCategoryResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<GetDisplayCategoryResponse>> getDisplayCategories() {
    List<GetDisplayCategoryResponse> categories = this.service.getCategories().stream()
        .map(DisplayCategoryMapper::convert).collect(Collectors.toList());

    return ResponseEntity.ok().body(categories);
  }
}
