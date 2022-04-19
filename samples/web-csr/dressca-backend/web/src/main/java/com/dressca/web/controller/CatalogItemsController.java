package com.dressca.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.dressca.applicationcore.catalog.CatalogApplicationService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.web.controller.dto.CatalogItemDto;
import com.dressca.web.controller.dto.PagedCatalogItemDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * CatalogItem の情報にアクセスする API コントローラーです。
 */
@RestController
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
public class CatalogItemsController {
    private CatalogApplicationService service;
    
    /**
     * カタログアイテムを検索して返します。
     * @param brandId ブランドID
     * @param categoryId カテゴリID
     * @param page ページ番号。未指定の場合は1。
     * @param pageSize ページサイズ。未指定の場合は20。
     * @return カタログアイテムの一覧
     */
    @GetMapping()
    public ResponseEntity<PagedCatalogItemDto> getByQuery(@RequestParam(name ="brandId", required = false) long brandId, 
        @RequestParam(name="categoryId", required = false) long categoryId,
        @RequestParam(name="page", defaultValue = "1") int page, 
        @RequestParam(name="pageSize", defaultValue = "20") int pageSize) {
        List<CatalogItemDto> items = service.getCatalogItems(brandId, categoryId, page, pageSize).stream()
            .map(this::convertCatalogItemDto)
            .collect(Collectors.toList());
        int totalCount = service.countCatalogItems(brandId, categoryId);

        PagedCatalogItemDto returnValue = new PagedCatalogItemDto(items, totalCount, page, pageSize);
        return ResponseEntity.ok().body(returnValue);
    }
    
    private CatalogItemDto convertCatalogItemDto(CatalogItem item) {
        return new CatalogItemDto(item.getDescription(), item.getPrice(), item.getCatalogCategoryId(), item.getCatalogBrandId());      
    }
}
