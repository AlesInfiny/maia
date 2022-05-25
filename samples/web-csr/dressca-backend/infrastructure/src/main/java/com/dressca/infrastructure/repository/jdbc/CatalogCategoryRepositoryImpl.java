package com.dressca.infrastructure.repository.jdbc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcCatalogCategoryRepository;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogCategoryEntity;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CatalogCategoryRepositoryImpl implements CatalogCategoryRepository{

    private JdbcCatalogCategoryRepository repository;

    @Override
    public List<CatalogCategory> getAll() {
        Iterable<CatalogCategoryEntity> entities = repository.findAll();
        List<CatalogCategory> catalogCategories = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogCategory)
            .collect(Collectors.toList());
        
        return catalogCategories;
    }

    private CatalogCategory toCatalogCategory(CatalogCategoryEntity entity) {
        return new CatalogCategory(entity.getName());
    }
    
}
