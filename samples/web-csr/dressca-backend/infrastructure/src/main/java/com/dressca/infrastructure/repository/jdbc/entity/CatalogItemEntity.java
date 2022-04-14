package com.dressca.infrastructure.repository.jdbc.entity;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("catalog_items")
@Data
public class CatalogItemEntity {
  private long id;
  private String name;
  private double price;
  private String productCode;
  private long catalogCategoryId;
  private long catalogBrandId;
}
