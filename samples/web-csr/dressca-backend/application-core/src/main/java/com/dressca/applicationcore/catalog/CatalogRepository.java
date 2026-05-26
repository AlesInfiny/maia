package com.dressca.applicationcore.catalog;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * カタログのリポジトリのインターフェースです。
 */
public interface CatalogRepository {

  List<CatalogItem> findByCategoryIdIn(List<UUID> categoryIds);

  List<CatalogItem> findByBrandIdAndCategoryId(UUID brandId, UUID categoryId, int page,
      int pageSize);

  List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId,
      int page, int pageSize);

  List<CatalogItem> findByCatalogItemIdIn(List<UUID> catalogItemIds);

  List<CatalogItem> findByCatalogItemIdInIncludingDeleted(List<UUID> catalogItemIds);

  int countByBrandIdAndCategoryId(UUID brandId, UUID categoryId);

  int countByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId);

  List<CatalogItem> findWithPaging(int skipRows, int pageSize);

  CatalogItem findById(UUID id);

  CatalogItem findByIdIncludingDeleted(UUID id);

  CatalogItem add(CatalogItem item);

  int remove(UUID id, OffsetDateTime rowVersion);

  int update(CatalogItem item);

  List<CatalogItem> findDeletedItemsByCatalogItemIdIn(List<UUID> catalogItemIds);
}
