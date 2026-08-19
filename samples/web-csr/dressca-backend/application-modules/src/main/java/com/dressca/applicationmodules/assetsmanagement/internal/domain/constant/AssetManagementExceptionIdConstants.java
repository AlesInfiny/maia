package com.dressca.applicationmodules.assetsmanagement.internal.domain.constant;

/**
 * アセット管理コンテキストの業務例外 ID 用の定数クラスです。
 */
public class AssetManagementExceptionIdConstants {
  /** 存在しないアセットコード: {0} のアセットが要求されました。 */
  public static final String E_ASSET_NOT_FOUND = "assetNotFound";

  /** サポートされていないアセットタイプ： {0} が指定されました。 */
  public static final String E_ASSET_TYPE_NOT_SUPPORTED = "assetTypeNotSupported";
}
