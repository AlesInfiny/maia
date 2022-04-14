package com.dressca.infrastructure.repository.jdbc;

import com.dressca.infrastructure.repository.jdbc.entity.BasketEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcBasketRepository extends CrudRepository<BasketEntity, Long>{
  
}
