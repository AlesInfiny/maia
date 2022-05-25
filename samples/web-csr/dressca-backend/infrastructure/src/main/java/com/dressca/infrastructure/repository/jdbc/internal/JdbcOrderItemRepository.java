package com.dressca.infrastructure.repository.jdbc.internal;

import com.dressca.infrastructure.repository.jdbc.internal.entity.OrderItemEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcOrderItemRepository extends CrudRepository<OrderItemEntity, Long> {
  
}
