package com.dressca.web.controller;

import java.util.List;

import com.dressca.applicationcore.catalog.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogBrand;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * CatalogBrand の情報にアクセスする API コントローラーです。
 */
@RestController
@RequestMapping("/api/catalog-brands")
@AllArgsConstructor
public class CatalogBrandsController {
    private CatalogApplicationService service;

    /**
     * カタログブランドの一覧を取得します。
     * @return カタログブランドの一覧
     */
    @GetMapping()
    public ResponseEntity<List<CatalogBrand>> getCatalogBrand() {
        List<CatalogBrand> brands = this.service.getBrands();
        return ResponseEntity.ok()
            .body(brands);
    }
}
