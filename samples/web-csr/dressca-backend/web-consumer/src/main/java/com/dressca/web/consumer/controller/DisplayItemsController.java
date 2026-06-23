package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.DisplayApplicationService;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemResponse;
import com.dressca.web.consumer.controller.dto.displayitem.PagedListOfGetDisplayItemResponse;
import com.dressca.web.consumer.mapper.DisplayItemMapper;
import org.springframework.http.ProblemDetail;
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
import lombok.RequiredArgsConstructor;

/**
 * {@link DisplayItem} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "DisplayItems", description = "掲載品の情報にアクセスする API です。")
@RequestMapping("/api/display-items")
@RequiredArgsConstructor
public class DisplayItemsController {

  private final DisplayApplicationService service;

  /**
   * 掲載品を検索して返します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ番号。未指定の場合は 1 。
   * @param pageSize ページサイズ。未指定の場合は 20 。
   * @return 掲載品の一覧。
   */
  @Operation(summary = "掲載品を検索して返します。", description = "掲載品を検索して返します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PagedListOfGetDisplayItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "リクエストエラー。",
          content = @Content(mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))})
  @GetMapping()
  public ResponseEntity<PagedListOfGetDisplayItemResponse> getByQuery(
      @RequestParam(name = "brandId", defaultValue = "0") long brandId,
      @RequestParam(name = "categoryId", defaultValue = "0") long categoryId,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
    List<GetDisplayItemResponse> items =
        service.getDisplayItems(brandId, categoryId, page, pageSize).stream()
            .map(DisplayItemMapper::convert).collect(Collectors.toList());
    int totalCount = service.countDisplayItems(brandId, categoryId);

    PagedListOfGetDisplayItemResponse returnValue =
        new PagedListOfGetDisplayItemResponse(items, totalCount, page, pageSize);
    return ResponseEntity.ok().body(returnValue);
  }
}
