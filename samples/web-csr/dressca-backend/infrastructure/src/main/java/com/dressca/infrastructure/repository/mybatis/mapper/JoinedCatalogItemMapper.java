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

  List<CatalogItem> findByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);

  List<CatalogItem> findByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId, @Param("limit") int limit, @Param("offset") int offset);

  List<CatalogItem> findByCatalogItemIdIn(@Param("catalogItemIds") List<Long> catalogItemIds);

  List<CatalogItem> findByCatalogItemIdInIncludingDeleted(
      @Param("catalogItemIds") List<Long> catalogItemIds);

  int countByBrandIdAndCategoryId(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId);

  int countByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") long brandId,
      @Param("categoryId") long categoryId);

  List<CatalogItem> findWithPaging(@Param("_skiprows") int skipRows,
      @Param("_pagesize") int pageSize);

  CatalogItem findById(@Param("id") long id);

  CatalogItem findByIdIncludingDeleted(@Param("id") long id);

}
