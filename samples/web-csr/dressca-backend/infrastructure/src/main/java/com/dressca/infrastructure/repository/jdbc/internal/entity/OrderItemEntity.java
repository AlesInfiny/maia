package com.dressca.infrastructure.repository.jdbc.internal.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("ORDER_ITEMS")
@Data
public class OrderItemEntity {
  @Id
  private long id;
  private long orderedCatalogItemId;
  private String orderedProductName;
  private String orderedProductCode;
  private BigDecimal unitPrice;
  private int quantity;
  private long orderId;
}
