package com.dressca.domainmodules.catalogmanagement.constant;

/**
 * カタログ管理コンテキストの業務例外 ID 用の定数クラスです。
 */
public class CatalogManagementExceptionIdConstants {
  /** ブランドID: {0} のブランドが見つかりませんでした。 */
  public static final String E_CATALOG_BRAND_NOT_FOUND = "catalogBrandNotFound";

  /** カテゴリID: {0} のカテゴリが見つかりませんでした。 */
  public static final String E_CATALOG_CATEGORY_NOT_FOUND = "catalogCategoryNotFound";

  /** 商品ID: {0} の商品が見つかりませんでした。 */
  public static final String E_CATALOG_ID_NOT_FOUND = "catalogIdNotFound";

  /** カタログアイテム ID: {0} の {1} 実行時に楽観ロックエラーが発生しました。 */
  public static final String E_OPTIMISTIC_LOCKING_FAILURE = "optimisticLockingFailure";
}
