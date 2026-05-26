package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.catalog.CatalogItem;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * カタログアイテムのテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedCatalogItemMapper {
  List<CatalogItem> findByCategoryIdIn(@Param("categoryIds") List<UUID> categoryIds);

  List<CatalogItem> findByBrandIdAndCategoryId(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId, @Param("limit") int limit,
      @Param("offset") int offset);

  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId, @Param("limit") int limit,
      @Param("offset") int offset);

  List<CatalogItem> findByCatalogItemIdIn(@Param("catalogItemIds") List<UUID> catalogItemIds);

  List<CatalogItem> findByCatalogItemIdInIncludingDeleted(
      @Param("catalogItemIds") List<UUID> catalogItemIds);

  int countByBrandIdAndCategoryId(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId);

  int countByBrandIdAndCategoryIdIncludingDeleted(@Param("brandId") UUID brandId,
      @Param("categoryId") UUID categoryId);

  List<CatalogItem> findWithPaging(@Param("_skiprows") int skipRows,
      @Param("_pagesize") int pageSize);

  CatalogItem findById(@Param("id") UUID id);

  CatalogItem findByIdIncludingDeleted(@Param("id") UUID id);

  List<CatalogItem> findDeletedItemsByCatalogItemIdIn(
      @Param("catalogItemIds") List<UUID> catalogItemIds);
}
