package com.dressca.applicationcore.catalog;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * カタログのリポジトリのインターフェースです。
 */
public interface CatalogRepository {

  /**
   * カテゴリ ID のリストに一致するカタログのリストを取得します。
   * 
   * @param categoryIds 検索対象のカタログ ID のリスト。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds);

  /**
   * ブランド ID とカテゴリ ID に一致するカタログのリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page,
      int pageSize);

  /**
   * 削除済みのカタログアイテムを含めて、ブランドIDとカテゴリIDに一致するカタログのリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(long brandId, long categoryId,
      int page, int pageSize);

  /**
   * カタログアイテム ID のリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテム ID 。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds);

  /**
   * 削除済みのカタログアイテムを含めて、カタログアイテム ID のリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテム ID 。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdInIncludingDeleted(List<Long> catalogItemIds);

  /**
   * ブランド ID とカテゴリ ID に一致するカタログの件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryId(long brandId, long categoryId);

  /**
   * 削除済みカタログアイテムを含めて、ブランド ID とカテゴリ ID に一致するカタログの件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryIdIncludingDeleted(long brandId, long categoryId);

  /**
   * バッチ向けにページングを考慮して全件データを取得します。
   * 
   * @param skipRows データ取得をスキップする行数。
   * @param pageSize データ取得行数の最大値。
   */
  List<CatalogItem> findWithPaging(int skipRows, int pageSize);

  /**
   * 指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID。
   * @return 条件に一致するカタログアイテム。
   */
  CatalogItem findById(long id);

  /**
   * 削除済みカタログアイテムを含めて、指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID。
   * @return 条件に一致するカタログアイテム。
   */
  CatalogItem findByIdIncludingDeleted(long id);

  /**
   * カタログアイテムを追加します。
   *
   * @param item カタログアイテム。
   * @return 追加されたカタログアイテム。
   */
  CatalogItem add(CatalogItem item);

  /**
   * カタログアイテムを削除します。
   * 
   * @param id カタログアイテム ID 。
   * @param rowVersion 行バージョン。
   * @return 削除できたら 1 、できなければ 0 を返す。
   */
  int remove(Long id, OffsetDateTime rowVersion);

  /**
   * カタログアイテムを更新します。
   * 
   * @param item カタログアイテム。
   * @return 更新できたら 1 、できなければ 0 を返す。
   */
  int update(CatalogItem item);
}
