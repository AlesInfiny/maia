package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.displayitem.DisplayItem;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 陳列品のテーブルにアクセスするためのマッパーのインターフェースです。
 * 陳列品テーブル（display_items）とカタログアイテムテーブル（catalog_items）を結合し、
 * 表示に必要な情報をカタログアイテムから解決します。
 */
@Mapper
public interface JoinedDisplayItemMapper {

  /**
   * ブランド ID とカテゴリ ID を条件に陳列品を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset 取得開始位置。
   * @return 陳列品一覧。
   */
  List<DisplayItem> findByBrandIdAndCategoryId(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * ブランド ID とカテゴリ ID を条件に件数を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 件数。
   */
  int countByBrandIdAndCategoryId(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId);

  /**
   * 陳列品 ID 一覧に一致する陳列品を取得します。
   *
   * @param displayItemIds 陳列品 ID 一覧。
   * @return 陳列品一覧。
   */
  List<DisplayItem> findByDisplayItemIdIn(@Param("displayItemIds") List<UUID> displayItemIds);

  /**
   * 削除済みを含めて、陳列品 ID 一覧に一致する陳列品を取得します。
   *
   * @param displayItemIds 陳列品 ID 一覧。
   * @return 陳列品一覧。
   */
  List<DisplayItem> findByDisplayItemIdInIncludingDeleted(
      @Param("displayItemIds") List<UUID> displayItemIds);

  /**
   * 削除済みの陳列品を ID 一覧で取得します。
   *
   * @param displayItemIds 陳列品 ID 一覧。
   * @return 陳列品一覧。
   */
  List<DisplayItem> findDeletedItemsByDisplayItemIdIn(
      @Param("displayItemIds") List<UUID> displayItemIds);
}
