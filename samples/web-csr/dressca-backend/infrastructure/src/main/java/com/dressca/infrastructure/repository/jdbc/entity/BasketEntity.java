package com.dressca.infrastructure.repository.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NonNull;

@Table("baskets")
@Data
public class BasketEntity {
  @Id
  private long id;
  @NonNull
  private String buyerId;
}
