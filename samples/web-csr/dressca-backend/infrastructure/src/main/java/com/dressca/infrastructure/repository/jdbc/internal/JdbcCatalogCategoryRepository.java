package com.dressca.infrastructure.repository.jdbc.internal;

import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogCategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogCategoryRepository extends CrudRepository<CatalogCategoryEntity, Long> {
  
}
