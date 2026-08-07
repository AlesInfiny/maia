package com.dressca.domainmodules.catalogmanagement.internal.domain.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import com.dressca.domainmodules.catalogmanagement.model.CatalogItem;

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
  List<CatalogItem> findByCategoryIdIn(List<UUID> categoryIds);

  /**
   * 削除済みのカタログアイテムを含めて、ブランドIDとカテゴリIDに一致するカタログのリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId,
      int page, int pageSize);

  /**
   * 削除済みカタログアイテムを含めて、ブランド ID とカテゴリ ID に一致するカタログの件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId);

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
  CatalogItem findById(UUID id);

  /**
   * 削除済みカタログアイテムを含めて、指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID。
   * @return 条件に一致するカタログアイテム。
   */
  CatalogItem findByIdIncludingDeleted(UUID id);

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
  int remove(UUID id, OffsetDateTime rowVersion);

  /**
   * カタログアイテムを更新します。
   *
   * @param item カタログアイテム。
   * @return 更新できたら 1 、できなければ 0 を返す。
   */
  int update(CatalogItem item);
}
