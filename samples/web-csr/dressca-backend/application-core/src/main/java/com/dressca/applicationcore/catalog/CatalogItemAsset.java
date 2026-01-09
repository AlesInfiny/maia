package com.dressca.applicationcore.catalog;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログアイテムアセットのエンティティです。
 */
@Data
public class CatalogItemAsset {
  private long id;
  private long catalogItemId;
  private String assetCode;

  /**
   * {@link CatalogItemAsset} クラスのインスタンスを初期化します。
   */
  public CatalogItemAsset() {
  }

  /**
   * {@link CatalogItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @param assetCode アセットコード 。
   */
  public CatalogItemAsset(long catalogItemId, @NonNull String assetCode) {
    this.catalogItemId = catalogItemId;
    this.assetCode = assetCode;
  }

  /**
   * {@link CatalogItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param id ID 。
   * @param catalogItemId カタログアイテム ID 。
   * @param assetCode アセットコード。
   */
  public CatalogItemAsset(long id, long catalogItemId, @NonNull String assetCode) {
    this.id = id;
    this.catalogItemId = catalogItemId;
    this.assetCode = assetCode;
  }
}
