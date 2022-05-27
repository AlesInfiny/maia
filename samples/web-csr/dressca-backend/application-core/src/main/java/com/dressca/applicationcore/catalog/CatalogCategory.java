package com.dressca.applicationcore.catalog;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログカテゴリのドメインモデル。
 */
@Data
public class CatalogCategory {
  private long id;
  @NonNull
  private String name;
  private List<CatalogItem> items = List.of();
}
