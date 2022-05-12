package com.dressca.infrastructure.repository.jdbc;

import java.util.List;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemAssetEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogItemAssetRepository extends CrudRepository<CatalogItemAssetEntity, Long> {

    List<CatalogItemAssetEntity> findByCatalogItemIdIn(List<Long> catalogItemIds);
}
