package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.DisplayApplicationService;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayBrandResponse;
import com.dressca.web.consumer.mapper.DisplayBrandMapper;
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
 * {@link DisplayBrand} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "DisplayBrands", description = "掲載ブランドの情報にアクセスする API です。")
@RequestMapping("/api/display-brands")
@RequiredArgsConstructor
public class DisplayBrandsController {

  private final DisplayApplicationService service;

  /**
   * 掲載ブランドの一覧を取得します。
   * 
   * @return 掲載ブランドの一覧。
   */
  @Operation(summary = "掲載ブランドの一覧を取得します。", description = "掲載ブランドの一覧を取得します。")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功。",
      content = @Content(mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = GetDisplayBrandResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<GetDisplayBrandResponse>> getDisplayBrands() {
    List<GetDisplayBrandResponse> brands = this.service.getBrands().stream()
        .map(DisplayBrandMapper::convert).collect(Collectors.toList());

    return ResponseEntity.ok().body(brands);
  }
}
