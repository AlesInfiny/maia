package com.dressca.applicationcore.displayitem;

import java.util.List;

/**
 * 掲載品のリポジトリインターフェースです。
 */
public interface DisplayRepository {
  /**
   * 削除済みの掲載品を、掲載品 ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 条件に一致する削除済み掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findDeletedItemsByDisplayItemIdIn(List<Long> displayItemIds);

  /**
   * 削除済みの掲載品を含めて、掲載品 ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdInIncludingDeleted(List<Long> displayItemIds);

  /**
   * 掲載品 ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdIn(List<Long> displayItemIds);

  /**
   * 削除済み掲載品を含めて、指定した ID の掲載品を取得します。
   *
   * @param id 掲載品ID。
   * @return 条件に一致する掲載品。
   */
  DisplayItem findByIdIncludingDeleted(long id);
}
