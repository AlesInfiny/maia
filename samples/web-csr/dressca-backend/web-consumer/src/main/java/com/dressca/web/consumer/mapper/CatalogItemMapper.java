package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemApiModel;

/**
 * {@link CatalogItem} と {@link CatalogItemApiModel} のマッパーです。
 */
public class CatalogItemMapper {

  /**
   * {@link CatalogItem} オブジェクトを {@link CatalogItemApiModel} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト。
   * @return {@link CatalogItemApiModel} オブジェクト。
   */
  public static CatalogItemApiModel convert(CatalogItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(CatalogItemAsset::getAssetCode).collect(Collectors.toList());

    return new CatalogItemApiModel(item.getId(), item.getName(), item.getProductCode(), assetCodes,
        item.getDescription(), item.getPrice(), item.getCatalogCategoryId(),
        item.getCatalogBrandId());
  }
}
