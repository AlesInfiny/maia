package com.dressca.applicationcore.catalog;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログブランドのドメインモデル。
 * カタログアイテムの製造元や企画元に基づいて定義されるブランドを表現します。
 */
@Data
@NoArgsConstructor
public class CatalogBrand {
  private long id;
  @NonNull
  private String name;
  private List<CatalogItem> items = new ArrayList<>();

  public CatalogBrand(@NonNull String name) {
    this.name = name;
  }
}
