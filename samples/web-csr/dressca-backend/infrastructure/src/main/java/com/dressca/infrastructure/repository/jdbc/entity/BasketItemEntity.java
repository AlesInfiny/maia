package com.dressca.infrastructure.repository.jdbc.entity;

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
  private double unitPrice;
  private int quantity;
}
