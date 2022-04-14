package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.OrderItemEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcOrderItemRepository extends CrudRepository<OrderItemEntity, Long> {
  
}
