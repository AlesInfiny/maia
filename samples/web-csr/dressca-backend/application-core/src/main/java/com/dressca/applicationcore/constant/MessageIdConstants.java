package com.dressca.applicationcore.constant;

/**
 * 業務メッセージ ID 用の定数クラスです。
 */
public class MessageIdConstants {

  /** アセット情報{0}を取得します。 */
  public static final String D_ASSET_GET_ASSET =
      "assetApplicationServiceGetAsset";

  /** 買い物かごに商品（顧客ID: {0}, カタログ商品ID: {1}, 数量: {2}）を追加します。 */
  public static final String D_SHOPPING_ADD_ITEM_TO_BASKET =
      "shoppingApplicationServiceAddItemToBasket";

  /** 買い物かごの商品の数量（顧客ID: {0}, 数量: {1}）を設定します。 */
  public static final String D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES =
      "shoppingApplicationServiceSetBasketItemsQuantities";

  /** 顧客（顧客ID: {0}）の買い物かごから商品（カタログ商品ID: {1}）を削除します。 */
  public static final String D_SHOPPING_DELETE_ITEM_FROM_BASKET =
      "shoppingApplicationServiceDeleteItemFromBasket";

  /** 顧客（顧客ID: {0}）の買い物かごと情報とその商品一覧を取得します。 */
  public static final String D_SHOPPING_GET_BASKET_ITEMS =
      "shoppingApplicationServiceGetBasketItems";

  /** 注文（顧客ID: {0}, お届け先: {1}）を確定します。 */
  public static final String D_SHOPPING_CHECKOUT =
      "shoppingApplicationServiceCheckout";

  /** 指定した注文ID: {0}, 購入者ID: {1} の注文情報を取得します。 */
  public static final String D_ORDER_GET_ORDER =
      "orderApplicationServiceGetOrder";

  /** 条件（ブランドID: {0}, カテゴリID: {1}, ページ: {2}, ページサイズ: {3}）に一致するカタログ情報を取得します。 */
  public static final String D_CATALOG_GET_CATALOG_ITEMS =
      "catalogApplicationServiceGetCatalogItems";

  /** 条件（ブランドID: {0}, カテゴリID: {1}）に一致するカテゴリの件数を取得します。 */
  public static final String D_CATALOG_COUNT_CATALOG_ITEMS =
      "catalogApplicationServiceCountCatalogItems";

  /** フィルタリング用のカタログブランドリストを取得します。 */
  public static final String D_CATALOG_GET_BRANDS =
      "catalogApplicationServiceGetBrands";

  /** フィルタリング用のカタログカテゴリリストを取得します。 */
  public static final String D_CATALOG_GET_CATEGORIES =
      "catalogApplicationServiceGetCategories";

  /** カタログアイテム ID: {0} のカタログアイテムを取得します。 */
  public static final String D_CATALOG_GET_CATALOG_ITEM =
      "catalogApplicationServiceGetCatalogItem";

  /** カタログにアイテムを追加します。 */
  public static final String D_CATALOG_ADD_ITEM_TO_CATALOG =
      "catalogApplicationServiceAddItemToCatalog";

  /** カタログアイテム ID: {0} のカタログアイテムを削除します。 */
  public static final String D_CATALOG_DELETE_ITEM_FROM_CATALOG =
      "catalogApplicationServiceDeleteItemFromCatalog";

  /** カタログアイテム ID: {0} のカタログアイテムを更新します。 */
  public static final String D_CATALOG_UPDATE_CATALOG_ITEM =
      "catalogApplicationServiceUpdateCatalogItem";
}
