package com.dressca.applicationcore.assets;

import lombok.Data;
import lombok.NonNull;

/**
 * アセットのドメインモデル
 */
@Data
public class Asset {

  // private long id;

  @NonNull
  private String assetCode;

  @NonNull
  private String assetType;

  // public String getAssetCode() {
  //   if (this.assetCode == null || this.assetCode.isEmpty()) {
  //     throw new IllegalArgumentException("AssetCodeが設定されていません");
  //   }
  //   return this.assetCode;
  // }

  // public void setAssetCode(String assetCode) {
  //   if (assetCode == null || this.assetCode.isEmpty()) {
  //     throw new IllegalArgumentException("AssetCodeが設定されていません");
  //   }
  //   this.assetCode = assetCode;
  // }

  // public String getAssetType() {
  //   if (this.assetType == null || this.assetType.isEmpty()) {
  //     throw new IllegalArgumentException("assetTypeが設定されていません");
  //   }
  //   return this.assetType;
  // }

  public void setAssetType(String assetType) {
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      throw new IllegalArgumentException("サポートされていないアセットタイプが指定されました。");
    }
    this.assetType = assetType;
  }
}
