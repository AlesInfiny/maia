package com.dressca.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcCatalogBrandRepository;
import com.dressca.infrastructure.repository.jdbc.entity.CatalogBrandEntity;

public class CatalogBrandRepositoryImpl implements CatalogBrandRepository{

    private JdbcCatalogBrandRepository repository;

    @Override
    public List<CatalogBrand> getAll() {
        Iterable<CatalogBrandEntity> entities = repository.findAll();
        List<CatalogBrand> catalogBrands = StreamSupport.stream(entities.spliterator(), false)
            .map(this::toCatalogBrand)
            .collect(Collectors.toList());
        return catalogBrands;
    }

    private CatalogBrand toCatalogBrand(CatalogBrandEntity entity) {
        return new CatalogBrand(entity.getName());
    }
    
}
