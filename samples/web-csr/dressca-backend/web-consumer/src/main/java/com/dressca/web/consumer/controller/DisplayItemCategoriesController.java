package com.dressca.web.consumer.controller;

import com.dressca.boundedcontexts.shopping.DisplayItemApplicationService;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemCategoriesResponse;
import com.dressca.web.consumer.mapper.DisplayItemCategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link DisplayItemCategory} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "DisplayItemCategories", description = "陳列品カテゴリの情報にアクセスする API です。")
@RequestMapping("/api/display-item-categories")
@RequiredArgsConstructor
public class DisplayItemCategoriesController {

  private final DisplayItemApplicationService service;

  /**
   * 陳列品カテゴリの一覧を取得します。
   *
   * @return 陳列品カテゴリの一覧。
   */
  @Operation(summary = "陳列品カテゴリの一覧を取得します。", description = "陳列品カテゴリの一覧を取得します。")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功。",
      content = @Content(mediaType = "application/json",
          array = @ArraySchema(
              schema = @Schema(implementation = GetDisplayItemCategoriesResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<GetDisplayItemCategoriesResponse>> getDisplayItemCategories() {
    List<GetDisplayItemCategoriesResponse> categories = this.service.getCategories().stream()
        .map(DisplayItemCategoryMapper::convert).collect(Collectors.toList());

    return ResponseEntity.ok().body(categories);
  }
}
