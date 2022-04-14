package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogCategoryEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogCategoryRepository extends CrudRepository<CatalogCategoryEntity, Long> {
  
}
