package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.BasketItemEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcBasketItemRepository extends CrudRepository<BasketItemEntity, Long> {
  
}
