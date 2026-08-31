package com.dressca.applicationmodules.catalogmanagement.internal.constant;

/**
 * カタログ管理コンテキストの業務メッセージ ID 用の定数クラスです。
 */
public class CatalogManagementMessageIdConstants {

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

  /** 条件（スキップ行数: {0}, ページサイズ: {1}）に一致するカタログ情報を取得します。 */
  public static final String D_CATALOG_GET_CATALOG_ITEMS_WITH_PAGING =
      "catalogApplicationServiceGetCatalogItemsWithPaging";

  /** フィルタリング用のカタログカテゴリリストを取得します。 */
  public static final String D_CATALOG_GET_CATEGORIES = "catalogApplicationServiceGetCategories";

  /** カタログアイテム ID: {0} のカタログアイテムを更新します。 */
  public static final String D_CATALOG_UPDATE_CATALOG_ITEM =
      "catalogApplicationServiceUpdateCatalogItem";
}
