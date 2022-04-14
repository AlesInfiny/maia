package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemAssetEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogItemAssetRepository extends CrudRepository<CatalogItemAssetEntity, Long> {
  
}
