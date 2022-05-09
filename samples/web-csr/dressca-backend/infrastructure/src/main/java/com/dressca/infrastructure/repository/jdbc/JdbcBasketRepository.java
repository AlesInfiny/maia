package com.dressca.infrastructure.repository.jdbc;

import java.util.Optional;
import com.dressca.infrastructure.repository.jdbc.entity.BasketEntity;

import org.springframework.data.repository.CrudRepository;

public interface JdbcBasketRepository extends CrudRepository<BasketEntity, Long>{
  Optional<BasketEntity> findByBuyerId(String buyerId);
}
