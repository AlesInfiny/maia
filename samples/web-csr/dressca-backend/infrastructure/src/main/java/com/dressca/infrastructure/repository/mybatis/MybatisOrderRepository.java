package com.dressca.infrastructure.repository.mybatis;

import com.dressca.infrastructure.repository.mybatis.mapper.JoinedOrderMapper;
import com.dressca.modules.order.Order;
import com.dressca.modules.order.OrderRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 注文情報のリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MybatisOrderRepository implements OrderRepository {

  @Autowired
  JoinedOrderMapper mapper;

  @Override
  public Order add(Order order) {
    mapper.add(order);
    long orderId = order.getId();
    mapper.addItems(orderId, order.getOrderItems());
    return mapper.findById(order.getId());
  }

  @Override
  public Optional<Order> findById(long id) {
    Order order = mapper.findById(id);
    return Optional.ofNullable(order);
  }
}
