package com.dressca.infrastructure.repository.jdbc.internal;

import java.util.List;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogItemRepository extends CrudRepository<CatalogItemEntity, Long> {

  List<CatalogItemEntity> findByIdIn(List<Long> ids);

  List<CatalogItemEntity> findByCatalogCategoryIdIn(List<Long> categoryIds);

  // List<CatalogItemEntity> findAll(Pageable pageable);
  
  List<CatalogItemEntity> findByCatalogBrandId(long brandId, Pageable pageable);

  List<CatalogItemEntity> findByCatalogCategoryId(long categoryId, Pageable pageable);

  List<CatalogItemEntity> findByCatalogBrandIdAndCatalogCategoryId(long brandId, long categoryId,
      Pageable pageable);

  int countByCatalogBrandIdAndCatalogCategoryId(long brandId, long categoryId);
}
