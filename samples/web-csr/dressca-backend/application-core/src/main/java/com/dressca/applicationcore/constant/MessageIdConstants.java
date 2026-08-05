package com.dressca.applicationcore.constant;

/**
 * 業務メッセージ ID 用の定数クラスです。
 */
public class MessageIdConstants {

  /** アセット情報{0}を取得します。 */
  public static final String D_ASSET_GET_ASSET = "assetApplicationServiceGetAsset";

  /** カタログにアイテムを追加します。 */
  public static final String D_CATALOG_ADD_ITEM_TO_CATALOG =
      "catalogApplicationServiceAddItemToCatalog";

  /** 条件（ブランドID: {0}, カテゴリID: {1}）に一致するカテゴリの件数を取得します。 */
  public static final String D_CATALOG_COUNT_CATALOG_ITEMS =
      "catalogApplicationServiceCountCatalogItems";

  /** カタログアイテム ID: {0} のカタログアイテムを削除します。 */
  public static final String D_CATALOG_DELETE_ITEM_FROM_CATALOG =
      "catalogApplicationServiceDeleteItemFromCatalog";

  /** フィルタリング用のカタログブランドリストを取得します。 */
  public static final String D_CATALOG_GET_BRANDS = "catalogApplicationServiceGetBrands";

  /** カタログアイテム ID: {0} のカタログアイテムを取得します。 */
  public static final String D_CATALOG_GET_CATALOG_ITEM = "catalogApplicationServiceGetCatalogItem";

  /** 条件（ブランドID: {0}, カテゴリID: {1}, ページ: {2}, ページサイズ: {3}）に一致するカタログ情報を取得します。 */
  public static final String D_CATALOG_GET_CATALOG_ITEMS =
      "catalogApplicationServiceGetCatalogItems";

  /** フィルタリング用のカタログカテゴリリストを取得します。 */
  public static final String D_CATALOG_GET_CATEGORIES = "catalogApplicationServiceGetCategories";

  /** カタログアイテム ID: {0} のカタログアイテムを更新します。 */
  public static final String D_CATALOG_UPDATE_CATALOG_ITEM =
      "catalogApplicationServiceUpdateCatalogItem";

  /** 条件（ブランドID: {0}, カテゴリID: {1}）に一致する陳列品の件数を取得します。 */
  public static final String D_DISPLAY_COUNT_DISPLAY_ITEMS =
      "displayApplicationServiceCountDisplayItems";

  /** フィルタリング用の陳列品ブランドリストを取得します。 */
  public static final String D_DISPLAY_GET_BRANDS = "displayApplicationServiceGetBrands";

  /** フィルタリング用の陳列品カテゴリリストを取得します。 */
  public static final String D_DISPLAY_GET_CATEGORIES = "displayApplicationServiceGetCategories";

  /** 条件（ブランドID: {0}, カテゴリID: {1}, ページ: {2}, ページサイズ: {3}）に一致する陳列品情報を取得します。 */
  public static final String D_DISPLAY_GET_DISPLAY_ITEMS =
      "displayApplicationServiceGetDisplayItems";

  /** 指定した注文ID: {0}, 購入者ID: {1} の注文情報を取得します。 */
  public static final String D_ORDER_GET_ORDER = "orderApplicationServiceGetOrder";

  /** 買い物かごに陳列品（顧客ID: {0}, 陳列品ID: {1}, 数量: {2}）を追加します。 */
  public static final String D_SHOPPING_ADD_ITEM_TO_BASKET =
      "shoppingApplicationServiceAddItemToBasket";

  /** 注文（顧客ID: {0}, お届け先: {1}）を確定します。 */
  public static final String D_SHOPPING_CHECKOUT = "shoppingApplicationServiceCheckout";

  /** 顧客（顧客ID: {0}）の買い物かごから陳列品（陳列品ID: {1}）を削除します。 */
  public static final String D_SHOPPING_DELETE_ITEM_FROM_BASKET =
      "shoppingApplicationServiceDeleteItemFromBasket";

  /** 顧客（顧客ID: {0}）の買い物かご情報とその陳列品一覧を取得します。 */
  public static final String D_SHOPPING_GET_BASKET_ITEMS =
      "shoppingApplicationServiceGetBasketItems";

  /** 買い物かごの陳列品の数量（顧客ID: {0}, 数量: {1}）を設定します。 */
  public static final String D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES =
      "shoppingApplicationServiceSetBasketItemsQuantities";
}
