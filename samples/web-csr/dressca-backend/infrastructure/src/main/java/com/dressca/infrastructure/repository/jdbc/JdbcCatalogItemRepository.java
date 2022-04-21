package com.dressca.infrastructure.repository.jdbc;

import java.util.List;

import com.dressca.infrastructure.repository.jdbc.entity.CatalogItemEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogItemRepository extends CrudRepository<CatalogItemEntity, Long> {

  List<CatalogItemEntity> findByIdIn(List<Long> Ids);

  List<CatalogItemEntity> findByCatalogCategoryIdIn(List<Long> categoryIds);

  List<CatalogItemEntity> findByCatalogBrandIdAndCatalogCategoryId(long brandId, long categoryId, Pageable pageable);

  int countByCatalogBrandIdAndCatalogCategoryId(long brandId, long categoryId);
}
