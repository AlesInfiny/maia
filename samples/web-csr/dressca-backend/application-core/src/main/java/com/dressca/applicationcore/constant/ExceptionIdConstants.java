package com.dressca.applicationcore.constant;

/**
 * 業務例外 ID 用の定数クラスです。
 */
public class ExceptionIdConstants {

  /** 存在しないアセットコード: {0} のアセットが要求されました。 */
  public static final String E_ASSET_NOT_FOUND =
      "assetNotFound";

  /** サポートされていないアセットタイプ： {0} が指定されました。 */
  public static final String E_ASSET_TYPE_NOT_SUPPORTED =
      "assetTypeNotSupported";

  /** "指定したアセットのアセットタイプ: {0} は Content-Type に変換できません。" */
  public static final String E_ASSET_TYPE_NOT_CONVERTED =
      "assetTypeNotConverted";

  /** 存在しない買い物かごID: {0} の買い物かごが要求されました。 */
  public static final String E_BASKET_IS_NULL_ON_CHECKOUT =
      "basketIsNullOnCheckout";

  /** 買い物かごID: {0} に商品ID: {1} の商品が見つかりませんでした。 */
  public static final String E_CATALOG_ITEM_ID_DOES_NOT_EXIST_IN_BASKET =
      "catalogItemIdDoesNotExistInBasket";

  /** 注文のチェックアウト時に買い物かごが空でした。 */
  public static final String E_BASKET_IS_EMPTY_ON_CHECKOUT =
      "basketIsEmptyOnCheckout";

  /** 存在しない注文情報（注文ID: {0}, 購入者ID: {1}）が要求されました。 */
  public static final String E_ORDER_NOT_FOUND =
      "orderNotFound";

  /** 商品ID: {0} の商品が見つかりませんでした。 */
  public static final String E_CATALOG_ID_NOT_FOUND =
      "catalogIdNotFound";

  /** ブランドID: {0} のブランドが見つかりませんでした。 */
  public static final String E_CATALOG_BRAND_NOT_FOUND =
      "catalogBrandNotFound";

  /** カテゴリID: {0} のカテゴリが見つかりませんでした。 */
  public static final String E_CATALOG_CATEGORY_NOT_FOUND =
      "catalogCategoryNotFound";

  /** {0} を実行する権限がありません。 */
  public static final String E_PERMISSION_DENIED =
      "permissionDenied";

  /** カタログアイテム ID: {0} の {1} 実行時に楽観ロックエラーが発生しました。 */
  public static final String E_OPTIMISTIC_LOCKING_FAILURE =
      "optimisticLockingFailure";
}
