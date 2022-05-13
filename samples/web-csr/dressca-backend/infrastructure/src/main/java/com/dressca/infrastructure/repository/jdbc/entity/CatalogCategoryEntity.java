package com.dressca.infrastructure.repository.jdbc.entity;

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
  @MappedCollection(idColumn = "catalog_category_id")
  private List<CatalogItemEntity> items;
}
