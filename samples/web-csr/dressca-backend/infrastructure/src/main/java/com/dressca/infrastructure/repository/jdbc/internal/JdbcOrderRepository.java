package com.dressca.infrastructure.repository.jdbc.internal;

import com.dressca.infrastructure.repository.jdbc.internal.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcOrderRepository extends CrudRepository<OrderEntity, Long> {
  
}
