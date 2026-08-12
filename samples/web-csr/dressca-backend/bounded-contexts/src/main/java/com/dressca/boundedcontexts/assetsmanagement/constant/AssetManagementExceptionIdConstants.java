package com.dressca.boundedcontexts.assetsmanagement.constant;

/**
 * アセット管理コンテキストの業務例外 ID 用の定数クラスです。
 */
public class AssetManagementExceptionIdConstants {
  /** 存在しないアセットコード: {0} のアセットが要求されました。 */
  public static final String E_ASSET_NOT_FOUND = "assetNotFound";

  /** 指定したアセットのアセットタイプ: {0} は Content-Type に変換できません。 */
  public static final String E_ASSET_TYPE_NOT_CONVERTED = "assetTypeNotConverted";

  /** サポートされていないアセットタイプ： {0} が指定されました。 */
  public static final String E_ASSET_TYPE_NOT_SUPPORTED = "assetTypeNotSupported";
}
