package com.dressca.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcCatalogItemRepository;
import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CatalogRepositoryImpl implements CatalogRepository {

    private JdbcCatalogItemRepository repository;

    @Override
    public List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds) {
        List<CatalogItemEntity> entities = repository.findByCatalogCategoryIdIn(categoryIds);
        List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogItem)
            .collect(Collectors.toList());

        return catalogItems;
    }

    @Override
    public List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<CatalogItemEntity> entities = repository.findByCatalogBrandIdAndCatalogCategoryId(brandId, categoryId, pageable);
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
        return repository.countByCatalogBrandIdAndCatalogCategoryId(brandId, categoryId);
    }

    private CatalogItem toCatalogItem(CatalogItemEntity entity) {
        return new CatalogItem(entity.getName(), 
            entity.getDescription(), 
            entity.getPrice(), 
            entity.getProductCode(),
            entity.getCatalogCategoryId(),
            entity.getCatalogBrandId());
    }
}
