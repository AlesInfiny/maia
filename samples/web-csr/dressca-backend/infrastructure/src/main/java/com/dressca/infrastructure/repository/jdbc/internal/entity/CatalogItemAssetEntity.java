package com.dressca.infrastructure.repository.jdbc.internal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("CATALOG_ITEM_ASSETS")
@Data
public class CatalogItemAssetEntity {
  @Id
  private long id;
  private String assetCode;
  private long catalogItemId;
}
