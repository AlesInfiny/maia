package com.dressca.applicationcore.assets;

import lombok.Data;
import lombok.NonNull;

/**
 * アセットのドメインモデル
 */
@Data
public class Asset {

  private long id;
  @NonNull
  private String assetCode;
  @NonNull
  private String assetType;

  public void setAssetType(String assetType) {
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      throw new IllegalArgumentException("サポートされていないアセットタイプが指定されました。");
    }
    this.assetType = assetType;
  }
}
