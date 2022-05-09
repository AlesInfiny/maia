package com.dressca.infrastructure.repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcOrderRepository;
import com.dressca.infrastructure.repository.jdbc.entity.OrderEntity;
import com.dressca.infrastructure.repository.jdbc.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
  
  private JdbcOrderRepository jdbcOrderRepository;

  @Override
  public Order add(Order order) {
    OrderEntity savedEntity = jdbcOrderRepository.save(toOrderEntity(order));
    return toOrder(savedEntity);
  }

  @Override
  public Optional<Order> findById(long id) {
    Optional<OrderEntity> entity = jdbcOrderRepository.findById(id);
    if (entity.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(toOrder(entity.get()));
  }

  private OrderEntity toOrderEntity(Order order) {
    OrderEntity entity = new OrderEntity();
    entity.setId(order.getId());
    entity.setBuyerId(order.getBuyerId());
    entity.setOrderDate(order.getOrderDate());
    entity.setShipToAddress(order.getShipToAddress().getFullName());
    entity.setConsumptionTaxRate(order.getConsumptionTaxRate());
    entity.setTotalItemsPrice(order.getTotalItemsPrice());
    entity.setDeliveryCharge(order.getDeliveryCharge());
    entity.setConsumptionTax(order.getConsumptionTax());
    entity.setTotalPrice(order.getTotalPrice());
    Set<OrderItemEntity> items = order.getOrderItems().stream()
      .map(this::toOrderItemEntity)
      .collect(Collectors.toSet());
    entity.setItems(items);
    return entity;
  }

  private OrderItemEntity toOrderItemEntity(OrderItem orderItem) {
    OrderItemEntity entity = new OrderItemEntity();
    entity.setId(orderItem.getId());
    entity.setOrderedCatalogItemId(orderItem.getItemOrdered().getCatalogItemId());
    entity.setOrderedProductName(orderItem.getItemOrdered().getProductName());
    entity.setOrderedProductCode(orderItem.getItemOrdered().getProductCode());
    entity.setUnitPrice(orderItem.getUnitPrice());
    entity.setQuantity(orderItem.getQuantity());
    entity.setOrderId(orderItem.getOrderId());
    return entity;
  }

  private Order toOrder(OrderEntity entity) {
    // TODO: 実装途中
    Order order = new Order();
    return order;
  }
}
