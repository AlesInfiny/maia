package com.dressca.infrastructure.repository.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("catalog_item_assets")
@Data
public class CatalogItemAssetEntity {
  @Id
  private long id;
  private String assetCode;
  private long orderItemId;
}
