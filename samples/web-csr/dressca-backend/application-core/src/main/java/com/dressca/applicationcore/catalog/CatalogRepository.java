package com.dressca.applicationcore.catalog;

import java.util.List;

/**
 * カタログリポジトリ。
 */
public interface CatalogRepository {

  /**
   * カテゴリIDのリストに一致するカタログのリストを取得します。
   * 
   * @param categoryIds 検索対象のカタログIDのリスト
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds);

  /**
   * ブランドIDとカテゴリIDに一致するカタログのリストを取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ
   * @param pageSize   ページサイズ
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page,
      int pageSize);

  /**
   * カタログアイテムIDのリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテムID
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds);

  /**
   * ブランドIDとカテゴリIDに一致するカタログの件数を取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryId(long brandId, long categoryId);

  /**
   * バッチ向けにページングを考慮して全件データを取得します。
   * 
   * @param skipRows データ取得をスキップする行数
   * @param pageSize データ取得行数の最大値
   */
  List<CatalogItem> findWithPaging(int skipRows, int pageSize);

  /**
   * 指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID
   * @return 条件に一致するカタログアイテム
   */
  CatalogItem findById(long id);

  /**
   * カタログアイテムを追加します。
   *
   * @param item カタログアイテム
   * @return 追加されたカタログアイテム
   */
  CatalogItem add(CatalogItem item);

  /**
   * カタログアイテムを削除します。
   *
   * @param item カタログアイテム
   */
  void remove(CatalogItem item);

  /**
   * カタログアイテムを更新します。
   *
   * @param item カタログアイテム
   */
  int update(CatalogItem item);

}
