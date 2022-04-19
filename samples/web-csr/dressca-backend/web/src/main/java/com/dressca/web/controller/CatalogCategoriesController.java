package com.dressca.web.controller;

import java.util.List;

import com.dressca.applicationcore.catalog.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogCategory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * CatalogCategory の情報にアクセスする API コントローラーです。
 */
@RestController
@RequestMapping("/api/catalog-categories")
@AllArgsConstructor
public class CatalogCategoriesController {
    private CatalogApplicationService service;

    /**
     * カタログカテゴリの一覧を取得します。
     * @return カタログカテゴリの一覧。
     */
    public ResponseEntity<List<CatalogCategory>> getCatalogCategories() {
        List<CatalogCategory> categories = this.service.getCategories();
        return ResponseEntity.ok()
            .body(categories);
    }
}
