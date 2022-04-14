package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.OrderEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcOrderRepository extends CrudRepository<OrderEntity, Long> {
  
}
