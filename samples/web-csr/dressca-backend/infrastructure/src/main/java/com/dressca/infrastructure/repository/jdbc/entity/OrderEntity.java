package com.dressca.infrastructure.repository.jdbc.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("orders")
@Data
public class OrderEntity {
  @Id
  private long id;
  private String buyerId;
  private LocalDateTime orderDate;
  private String shipToAddress;
  private BigDecimal consumptionTaxRate;
  private BigDecimal totalItemsPrice;
  private BigDecimal deliveryCharge;
  private BigDecimal consumptionTax;
  private BigDecimal totalPrice;
  @MappedCollection(idColumn = "order_id")
  private Set<OrderItemEntity> items;
}
