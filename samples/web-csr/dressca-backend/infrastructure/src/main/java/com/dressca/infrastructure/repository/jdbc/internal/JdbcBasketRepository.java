package com.dressca.infrastructure.repository.jdbc.internal;

import java.util.Optional;
import com.dressca.infrastructure.repository.jdbc.internal.entity.BasketEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcBasketRepository extends CrudRepository<BasketEntity, Long>{
  Optional<BasketEntity> findFirstByBuyerId(String buyerId);
}
