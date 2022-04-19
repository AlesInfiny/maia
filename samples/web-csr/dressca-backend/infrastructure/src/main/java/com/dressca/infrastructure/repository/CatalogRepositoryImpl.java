package com.dressca.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcCatalogItemRepository;
import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemEntity;

import org.springframework.data.domain.Pageable;

public class CatalogRepositoryImpl implements CatalogRepository {

    private JdbcCatalogItemRepository repository;

    @Override
    public List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds) {
        List<CatalogItemEntity> entities = repository.findByCategoryIdIn(categoryIds);
        List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogItem)
            .collect(Collectors.toList());

        return catalogItems;
    }

    @Override
    public List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<CatalogItemEntity> entities = repository.findByBrandIdAndCategoryId(brandId, categoryId, pageable);
        List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogItem)
            .collect(Collectors.toList());

        return catalogItems;
    }

    @Override
    public List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds) {
        List<CatalogItemEntity> entities = repository.findByIdIn(catalogItemIds);
        List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogItem)
            .collect(Collectors.toList());

        return catalogItems;
    }

    @Override
    public int countByBrandIdAndCategoryId(long brandId, long categoryId) {
        return repository.countByBrandIdAndCategoryId(brandId, categoryId);
    }

    // TODO: descriptionの引継ぎが未実装
    private CatalogItem toCatalogItem(CatalogItemEntity entity) {
        return new CatalogItem(entity.getName(), 
            null, 
            entity.getPrice(), 
            entity.getProductCode(),
            entity.getCatalogCategoryId(),
            entity.getCatalogBrandId());
    }
}
