package com.dressca.applicationcore.catalog;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログアイテムアセットのドメインモデルです。
 */
@Data
public class CatalogItemAsset {
  private long id;
  // private CatalogItem catalogItem;
  private long catalogItemId;
  // @NonNull
  private String assetCode;
  
  public CatalogItemAsset() {
  }

  public CatalogItemAsset(long catalogItemId, @NonNull String assetCode) {
    this.catalogItemId = catalogItemId;
    this.assetCode = assetCode;
  }

  public CatalogItemAsset(long id, long catalogItemId, @NonNull String assetCode) {
    this.id = id;
    this.catalogItemId = catalogItemId;
    this.assetCode = assetCode;
  }
}
