package com.dressca.applicationmodules.catalogmanagement.internal.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationmodules.catalogmanagement.entity.CatalogItem;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * カタログアイテムのテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedCatalogItemMapper {
  /**
   * カテゴリ ID の一覧に一致するカタログアイテムを取得します。
   * 
   * @param categoryIds カテゴリ ID 一覧。
   * @return カタログアイテム一覧。
   */
  List<CatalogItem> findByCategoryIdIn(@Param("categoryIds") List<UUID> categoryIds);

  /**
   * 削除済みを含めて、ブランド ID とカテゴリ ID を条件にカタログアイテムを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param limit 取得件数。
   * @param offset 取得開始位置。
   * @return カタログアイテム一覧。
   */
  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId, @Param("limit") int limit, @Param("offset") int offset);

  /**
   * 削除済みを含めて、ブランド ID とカテゴリ ID を条件に件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 件数。
   */
  int countByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId);

  /**
   * ページング条件を指定してカタログアイテムを取得します。
   * 
   * @param skipRows 読み飛ばす行数。
   * @param pageSize 取得件数。
   * @return カタログアイテム一覧。
   */
  List<CatalogItem> findWithPaging(@Param("_skiprows") int skipRows,
      @Param("_pagesize") int pageSize);

  /**
   * ID を条件にカタログアイテムを取得します。
   * 
   * @param id カタログアイテム ID 。
   * @return カタログアイテム。
   */
  CatalogItem findById(@Param("id") UUID id);

  /**
   * 削除済みを含めて、ID を条件にカタログアイテムを取得します。
   * 
   * @param id カタログアイテム ID 。
   * @return カタログアイテム。
   */
  CatalogItem findByIdIncludingDeleted(@Param("id") UUID id);
}
