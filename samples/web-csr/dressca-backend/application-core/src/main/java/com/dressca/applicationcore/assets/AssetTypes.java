package com.dressca.applicationcore.assets;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * アセットタイプを管理します。
 */
public class AssetTypes {
  public static final String png = "png";
  private static final Set<String> supportedAssetTypes = Set.of(png);

  public static boolean isSupportedAssetTypes(String assetType) {
    if (StringUtils.isEmpty(assetType)) {
      return false;
    }
    return supportedAssetTypes.contains(assetType);
  }
}
