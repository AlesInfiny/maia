package com.dressca.applicationmodules.shopping.internal.domain.repository;

import com.dressca.applicationmodules.shopping.entity.DisplayItem;
import java.util.List;
import java.util.UUID;

/**
 * 陳列品のリポジトリのインターフェースです。
 */
public interface DisplayItemRepository {

  /**
   * ブランド ID とカテゴリ ID に一致する陳列品のリストを取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致する陳列品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByBrandIdAndCategoryId(UUID brandId, UUID categoryId, int page,
      int pageSize);

  /**
   * ブランド ID とカテゴリ ID に一致する陳列品の件数を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致する陳列品の件数。
   */
  int countByBrandIdAndCategoryId(UUID brandId, UUID categoryId);

  /**
   * 陳列品 ID のリストに一致する陳列品のリストを取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 条件に一致する陳列品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdIn(List<UUID> displayItemIds);

  /**
   * 削除済みの陳列品を含めて、陳列品 ID のリストに一致する陳列品のリストを取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 条件に一致する陳列品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdInIncludingDeleted(List<UUID> displayItemIds);

  /**
   * 削除済みの陳列品を、陳列品 ID のリストに一致する陳列品のリストで取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 条件に一致する削除済み陳列品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findDeletedItemsByDisplayItemIdIn(List<UUID> displayItemIds);
}
