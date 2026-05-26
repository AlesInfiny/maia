package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログカテゴリのエンティティです。
 */
@Data
@NoArgsConstructor
public class CatalogCategory {
  private UUID id;
  @NonNull
  private String name;
  private List<CatalogItem> items = List.of();

  /**
   * カタログカテゴリ名を指定して、 {@link CatalogCategory} クラスのインスタンスを初期化します。
   * 
   * @param name カタログカテゴリ名。
   */
  public CatalogCategory(@NonNull String name) {
    this.name = name;
  }
}
