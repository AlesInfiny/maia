package com.dressca.applicationcore.catalog;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログブランドのエンティティです。 カタログアイテムの製造元や企画元に基づいて定義されるブランドを表現します。
 */
@Data
@NoArgsConstructor
public class CatalogBrand {
  private long id;
  @NonNull
  private String name;
  private List<CatalogItem> items = new ArrayList<>();

  /**
   * カタログブランド名を指定して、 {@link CatalogBrand} クラスのインスタンスを初期化します。
   * 
   * @param name カタログブランド名。
   */
  public CatalogBrand(@NonNull String name) {
    this.name = name;
  }
}
