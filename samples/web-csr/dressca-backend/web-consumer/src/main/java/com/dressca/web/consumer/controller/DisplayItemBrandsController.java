package com.dressca.web.consumer.controller;

import com.dressca.applicationmodules.shopping.DisplayItemApplicationService;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemBrandsResponse;
import com.dressca.web.consumer.mapper.DisplayItemBrandMapper;
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
 * {@link DisplayItemBrand} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "DisplayItemBrands", description = "陳列品ブランドの情報にアクセスする API です。")
@RequestMapping("/api/display-item-brands")
@RequiredArgsConstructor
public class DisplayItemBrandsController {

  private final DisplayItemApplicationService service;

  /**
   * 陳列品ブランドの一覧を取得します。
   *
   * @return 陳列品ブランドの一覧。
   */
  @Operation(summary = "陳列品ブランドの一覧を取得します。", description = "陳列品ブランドの一覧を取得します。")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功。",
      content = @Content(mediaType = "application/json",
          array = @ArraySchema(
              schema = @Schema(implementation = GetDisplayItemBrandsResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<GetDisplayItemBrandsResponse>> getDisplayItemBrands() {
    List<GetDisplayItemBrandsResponse> brands = this.service.getBrands().stream()
        .map(DisplayItemBrandMapper::convert).collect(Collectors.toList());

    return ResponseEntity.ok().body(brands);
  }
}
