package com.dressca.infrastructure.repository.jdbc.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("CATALOG_BRANDS")
@Data
public class CatalogBrandEntity {
  @Id
  private long id;
  private String name;
  @MappedCollection(idColumn = "catalog_brand_id")
  private List<CatalogItemEntity> items;
}
