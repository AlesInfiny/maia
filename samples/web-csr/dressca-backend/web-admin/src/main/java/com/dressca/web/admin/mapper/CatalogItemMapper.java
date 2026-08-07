package com.dressca.web.admin.mapper;

import com.dressca.domainmodules.catalogmanagement.model.CatalogItem;
import com.dressca.domainmodules.catalogmanagement.model.CatalogItemAsset;
import com.dressca.web.admin.controller.dto.catalog.GetCatalogItemResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link CatalogItem} と {@link GetCatalogItemResponse} のマッパーです。
 */
public class CatalogItemMapper {

  /**
   * {@link CatalogItem} オブジェクトを {@link GetCatalogItemResponse} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト。
   * @return {@link GetCatalogItemResponse} オブジェクト。
   */
  public static GetCatalogItemResponse convert(CatalogItem item) {

    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(CatalogItemAsset::getAssetCode).collect(Collectors.toList());

    return new GetCatalogItemResponse(item.getId(), item.getName(), item.getProductCode(),
        assetCodes, item.getDescription(), item.getPrice(), item.getCatalogCategoryId(),
        item.getCatalogBrandId(), item.getRowVersion(), item.isDeleted());
  }
}
