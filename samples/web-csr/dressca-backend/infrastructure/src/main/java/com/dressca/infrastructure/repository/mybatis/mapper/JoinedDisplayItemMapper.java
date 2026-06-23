package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.displayitem.DisplayItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 掲載品のテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedDisplayItemMapper {
  /**
   * カテゴリ ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param categoryIds 検索対象の掲載品 ID のリスト。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);

  /**
   * ブランド ID とカテゴリ ID に一致する掲載品のリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset オフセット。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * 削除済みの掲載品を含めて、ブランドIDとカテゴリIDに一致する掲載品のリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset オフセット。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * 掲載品 ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdIn(@Param("displayItemIds") List<Long> displayItemIds);

  /**
   * 削除済みの掲載品を含めて、掲載品 ID のリストに一致する掲載品のリストを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 条件に一致する掲載品のリスト。存在しない場合、空のリスト。
   */
  List<DisplayItem> findByDisplayItemIdInIncludingDeleted(
      @Param("displayItemIds") List<Long> displayItemIds);

  /**
   * ブランド ID とカテゴリ ID に一致する掲載品の件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致する掲載品の件数。
   */
  int countByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId);

  /**
   * 指定した ID の掲載品を取得します。
   *
   * @param id 掲載品ID。
   * @return 条件に一致する掲載品。
   */
  DisplayItem findById(@Param("id") long id);

  /**
   * 削除済み掲載品を含めて、指定した ID の掲載品を取得します。
   *
   * @param id 掲載品ID。
   * @return 条件に一致する掲載品。
   */
  DisplayItem findByIdIncludingDeleted(@Param("id") long id);

  /**
   * 削除済み掲載品を取得します。
   *
   * @param displayItemIds 掲載品IDのリスト。
   * @return 条件に一致する削除済み掲載品のリスト。
   */
  List<DisplayItem> findDeletedItemsByDisplayItemIdIn(
      @Param("displayItemIds") List<Long> displayItemIds);
}
