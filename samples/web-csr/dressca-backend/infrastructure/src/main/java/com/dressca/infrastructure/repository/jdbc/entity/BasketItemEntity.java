package com.dressca.infrastructure.repository.jdbc.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("basket_items")
@Data
public class BasketItemEntity {
  @Id
  private long id;
  private long basketId;
  private long catalogItemId;
  private BigDecimal unitPrice;
  private int quantity;
}
