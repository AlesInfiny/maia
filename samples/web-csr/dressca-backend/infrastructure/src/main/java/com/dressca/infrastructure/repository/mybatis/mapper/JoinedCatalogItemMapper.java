package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.catalog.CatalogItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * カタログアイテムのテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedCatalogItemMapper {
  /**
   * カテゴリ ID のリストに一致するカタログのリストを取得します。
   * 
   * @param categoryIds 検索対象のカタログ ID のリスト。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);

  /**
   * ブランド ID とカテゴリ ID に一致するカタログのリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset オフセット。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * 削除済みのカタログアイテムを含めて、ブランドIDとカテゴリIDに一致するカタログのリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset オフセット。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * カタログアイテム ID のリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテム ID 。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdIn(@Param("catalogItemIds") List<Long> catalogItemIds);

  /**
   * 削除済みのカタログアイテムを含めて、カタログアイテム ID のリストに一致するカタログのリストを取得します。
   * 
   * @param catalogItemIds カタログアイテム ID 。
   * @return 条件に一致するカタログのリスト。存在しない場合、空のリスト。
   */
  List<CatalogItem> findByCatalogItemIdInIncludingDeleted(
      @Param("catalogItemIds") List<Long> catalogItemIds);

  /**
   * ブランド ID とカテゴリ ID に一致するカタログの件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId);

  /**
   * 削除済みカタログアイテムを含めて、ブランド ID とカテゴリ ID に一致するカタログの件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログの件数。
   */
  int countByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId);

  /**
   * バッチ向けにページングを考慮して全件データを取得します。
   * 
   * @param skipRows データ取得をスキップする行数。
   * @param pageSize データ取得行数の最大値。
   */
  List<CatalogItem> findWithPaging(@Param("_skiprows") int skipRows,
      @Param("_pagesize") int pageSize);

  /**
   * 指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID。
   * @return 条件に一致するカタログアイテム。
   */
  CatalogItem findById(@Param("id") long id);

  /**
   * 削除済みカタログアイテムを含めて、指定した ID のカタログアイテムを取得します。
   *
   * @param id カタログアイテムID。
   * @return 条件に一致するカタログアイテム。
   */
  CatalogItem findByIdIncludingDeleted(@Param("id") long id);
}
