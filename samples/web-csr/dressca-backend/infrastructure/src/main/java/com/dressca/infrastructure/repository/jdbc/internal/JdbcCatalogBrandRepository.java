package com.dressca.infrastructure.repository.jdbc.internal;

import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogBrandEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcCatalogBrandRepository extends CrudRepository<CatalogBrandEntity, Long> {
  
}
