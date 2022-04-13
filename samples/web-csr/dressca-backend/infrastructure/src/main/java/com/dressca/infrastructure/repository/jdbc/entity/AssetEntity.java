package com.dressca.infrastructure.repository.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("ASSET")
@Data
public class AssetEntity {
  @Id
  private long id;
  private String assetCode;
  private String assetType;
}
