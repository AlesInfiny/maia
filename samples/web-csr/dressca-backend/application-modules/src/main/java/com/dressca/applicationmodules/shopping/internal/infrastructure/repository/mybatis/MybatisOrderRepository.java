package com.dressca.applicationmodules.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.applicationmodules.shopping.entity.Order;
import com.dressca.applicationmodules.shopping.internal.domain.repository.OrderRepository;
import com.dressca.applicationmodules.shopping.internal.infrastructure.repository.mybatis.mapper.JoinedOrderMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 注文情報のリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisOrderRepository implements OrderRepository {

  private final JoinedOrderMapper mapper;

  @Override
  public Order add(Order order) {
    mapper.add(order);
    UUID orderId = order.getId();
    mapper.addItems(orderId, order.getOrderItems());
    return mapper.findById(orderId);
  }

  @Override
  public Optional<Order> findById(UUID id) {
    Order order = mapper.findById(id);
    return Optional.ofNullable(order);
  }
}
