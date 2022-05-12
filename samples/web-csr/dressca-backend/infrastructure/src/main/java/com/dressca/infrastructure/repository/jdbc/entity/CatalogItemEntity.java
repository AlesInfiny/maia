package com.dressca.infrastructure.repository.jdbc.entity;

import java.math.BigDecimal;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("CATALOG_ITEMS")
@Data
public class CatalogItemEntity {
  private long id;
  private String name;
  private String description;
  private BigDecimal price;
  private String productCode;
  private long catalogCategoryId;
  private long catalogBrandId;
}
