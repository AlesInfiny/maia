package com.dressca.infrastructure.repository.jdbc.internal.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("CATALOG_CATEGORIES")
@Data
public class CatalogCategoryEntity {
  @Id
  private long id;
  private String name;
  // @MappedCollection(idColumn = "CATALOG_CATEGORY_ID", keyColumn = "CATALOG_CATEGORY_ID")
  // private List<CatalogItemEntity> items;
}
