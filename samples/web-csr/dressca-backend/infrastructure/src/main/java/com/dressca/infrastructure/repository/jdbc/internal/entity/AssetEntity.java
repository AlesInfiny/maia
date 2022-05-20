package com.dressca.infrastructure.repository.jdbc.internal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("ASSETS")
@Data
public class AssetEntity {
  @Id
  private long id;
  private String assetCode;
  private String assetType;
}
