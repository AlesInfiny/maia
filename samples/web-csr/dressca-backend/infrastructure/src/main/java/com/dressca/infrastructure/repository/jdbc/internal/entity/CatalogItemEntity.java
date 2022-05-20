package com.dressca.infrastructure.repository.jdbc.internal.entity;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;
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
  @MappedCollection(idColumn = "CATALOG_ITEM_ID", keyColumn = "CATALOG_ITEM_ID")
  private List<CatalogItemAssetEntity> assets;
}
