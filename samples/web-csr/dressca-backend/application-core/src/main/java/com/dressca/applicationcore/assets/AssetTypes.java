package com.dressca.applicationcore.assets;

import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * アセットタイプを管理するクラスです。
 */
public class AssetTypes {
  public static final String PNG = "png";
  private static final Set<String> supportedAssetTypes = Set.of(PNG);

  /**
   * アセットタイプが対応しているかどうかを判定します。
   * 
   * @param assetType アセットタイプ。
   * @return 対応していれば true、そうでなければ false 。
   */
  public static boolean isSupportedAssetTypes(String assetType) {
    if (StringUtils.isEmpty(assetType)) {
      return false;
    }
    return supportedAssetTypes.contains(assetType);
  }
}
