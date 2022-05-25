package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.infrastructure.repository.mybatis.mapper.OrderMapper;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class MybatisOrderRepository implements OrderRepository {

  @Autowired
  OrderMapper mapper;

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
    if (order == null) {
      return Optional.empty();
    }
    return Optional.of(order);
  }
}
