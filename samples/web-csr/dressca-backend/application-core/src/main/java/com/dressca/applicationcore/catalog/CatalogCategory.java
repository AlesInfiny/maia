package com.dressca.applicationcore.catalog;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログカテゴリのドメインモデル。
 */
@Data
@NoArgsConstructor
public class CatalogCategory {
  private long id;
  @NonNull
  private String name;
  private List<CatalogItem> items = List.of();
  
  public CatalogCategory(@NonNull String name) {
    this.name = name;
  }
}
