package com.dressca.infrastructure.repository.jdbc;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcCatalogBrandRepository;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogBrandEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CatalogBrandRepositoryImpl implements CatalogBrandRepository {

  private JdbcCatalogBrandRepository repository;

  @Override
  public List<CatalogBrand> getAll() {
    Iterable<CatalogBrandEntity> entities = repository.findAll();
    List<CatalogBrand> catalogBrands = StreamSupport.stream(entities.spliterator(), false)
        .map(this::toCatalogBrand).collect(Collectors.toList());
    return catalogBrands;
  }

  private CatalogBrand toCatalogBrand(CatalogBrandEntity entity) {
    CatalogBrand catalogBrand = new CatalogBrand(entity.getName());
    catalogBrand.setId(entity.getId());
    return catalogBrand;
  }
}
