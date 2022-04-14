package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogItemRepository extends CrudRepository<CatalogItemEntity, Long> {
  
}
