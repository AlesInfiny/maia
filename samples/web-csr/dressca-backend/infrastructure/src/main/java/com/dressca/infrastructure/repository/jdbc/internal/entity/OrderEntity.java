package com.dressca.infrastructure.repository.jdbc.internal.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("ORDERS")
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
  // @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_ID")
  // private Set<OrderItemEntity> items;
}
