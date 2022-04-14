package com.dressca.infrastructure.repository.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("catalog_brands")
@Data
public class CatalogBrandEntity {
  @Id
  private long id;
  private String name;
}
