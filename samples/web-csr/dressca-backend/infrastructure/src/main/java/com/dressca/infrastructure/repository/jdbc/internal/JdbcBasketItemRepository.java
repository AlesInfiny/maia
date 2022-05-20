package com.dressca.infrastructure.repository.jdbc.internal;

import com.dressca.infrastructure.repository.jdbc.internal.entity.BasketItemEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcBasketItemRepository extends CrudRepository<BasketItemEntity, Long> {
  
}
