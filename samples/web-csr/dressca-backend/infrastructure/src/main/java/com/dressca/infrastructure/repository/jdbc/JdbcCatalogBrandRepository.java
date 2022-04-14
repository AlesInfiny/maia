package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogBrandEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogBrandRepository extends CrudRepository<CatalogBrandEntity, Long> {
  
}
