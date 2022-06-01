package com.dressca.applicationcore.assets;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * アセットのドメインモデル。
 */
@Data
@NoArgsConstructor
public class Asset {

  private long id;
  @NonNull
  private String assetCode;
  @NonNull
  private String assetType;

  /**
   * コンストラクタ。
   * 
   * @param assetCode アセットコード
   * @param assetType アセットタイプ
   */
  public Asset(@NonNull String assetCode, @NonNull String assetType) {
    this.assetCode = assetCode;
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      throw new IllegalArgumentException("サポートされていないアセットタイプが指定されました。");
    }
    this.assetType = assetType;
  }

  /**
   * コンストラクタ。
   * 
   * @param assetType アセットタイプ
   */
  public void setAssetType(String assetType) {
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      throw new IllegalArgumentException("サポートされていないアセットタイプが指定されました。");
    }
    this.assetType = assetType;
  }
}
