package com.dressca.web.controller;

import java.util.List;

import com.dressca.applicationcore.catalog.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogBrand;
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
 * {@link CatalogBrand} の情報にアクセスする API コントローラーです.
 */
@RestController
@Tag(name = "CatalogBrand", description = "カタログブランドの情報にアクセスするAPI")
@RequestMapping("/api/catalog-brands")
@AllArgsConstructor
public class CatalogBrandsController {

    @Autowired
    private CatalogApplicationService service;

    /**
     * カタログブランドの一覧を取得します.
     * 
     * @return カタログブランドの一覧
     */
    @Operation(summary = "カタログブランドの一覧を取得する.", description = "カタログブランドの一覧を取得する.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "成功",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CatalogBrand.class))))})
    @GetMapping()
    public ResponseEntity<List<CatalogBrand>> getCatalogBrand() {
        List<CatalogBrand> brands = this.service.getBrands();
        return ResponseEntity.ok().body(brands);
    }
}
