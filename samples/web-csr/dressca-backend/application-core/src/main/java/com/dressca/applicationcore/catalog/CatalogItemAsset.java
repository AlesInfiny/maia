package com.dressca.applicationcore.catalog;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログアイテムアセットのドメインモデルです。
 */
@Data
public class CatalogItemAsset {
  private CatalogItem catalogItem;
  @NonNull
  private String assetCode;
}
