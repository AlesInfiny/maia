package com.dressca.infrastructure.repository.jdbc.entity;

import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NonNull;

@Table("BASKETS")
@Data
public class BasketEntity {
  @Id
  private long id;
  @NonNull
  private String buyerId;
  @MappedCollection(idColumn = "basket_id")
  private Set<BasketItemEntity> items;
}
