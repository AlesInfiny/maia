package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemResponse;

/**
 * {@link CatalogItem} と {@link CatalogItemResponse} のマッパーです。
 */
public class CatalogItemMapper {

  /**
   * {@link CatalogItem} オブジェクトを {@link CatalogItemResponse} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト。
   * @return {@link CatalogItemResponse} オブジェクト。
   */
  public static CatalogItemResponse convert(CatalogItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(CatalogItemAsset::getAssetCode).collect(Collectors.toList());

    return new CatalogItemResponse(item.getId(), item.getName(), item.getProductCode(), assetCodes,
        item.getDescription(), item.getPrice(), item.getCatalogCategoryId(),
        item.getCatalogBrandId());
  }
}
