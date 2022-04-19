package com.dressca.applicationcore.catalog;

import java.util.List;

/**
 * カタログリポジトリ。
 */
public interface CatalogRepository {

  /**
   * カテゴリIDのリストに一致するカタログのリストを取得します。
   * 
   * @param categoryIds　検索対象のカタログIDのリスト
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds);
  
  /**
   * ブランドIDとカテゴリIDに一致するカタログのリストを取得します。
   * 
   * @param brandId ブランドID
   * @param categoryId カテゴリID
   * @param pageable ページング情報
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page, int pageSize);

  /**
   * カタログアイテムIDのリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテムID
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds);

  /**
   * ブランドIDとカテゴリIDに一致するカタログの件数を取得します。
   * @param brandId ブランドID
   * @param categoryId カテゴリID
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryId(long brandId, long categoryId);
}
