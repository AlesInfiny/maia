package com.dressca.web.consumer.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.web.consumer.controller.dto.catalog.CatalogBrandResponse;
import com.dressca.web.consumer.mapper.CatalogBrandMapper;
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
 * {@link CatalogBrand} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(
    name = "CatalogBrands",
    description = "カタログブランドの情報にアクセスする API です。")
@RequestMapping("/api/catalog-brands")
@AllArgsConstructor
public class CatalogBrandsController {

  @Autowired
  private CatalogApplicationService service;

  /**
   * カタログブランドの一覧を取得します。
   * 
   * @return カタログブランドの一覧。
   */
  @Operation(
      summary = "カタログブランドの一覧を取得します。",
      description = "カタログブランドの一覧を取得します。")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "成功。",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = CatalogBrandResponse.class))))})
  @GetMapping()
  public ResponseEntity<List<CatalogBrandResponse>> getCatalogBrands() {
    List<CatalogBrandResponse> brands = this.service.getBrands().stream()
        .map(CatalogBrandMapper::convert)
        .collect(Collectors.toList());

    return ResponseEntity.ok().body(brands);
  }
}
