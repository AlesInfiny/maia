package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryApiModel;

/**
 * {@link CatalogItem} と {@link CatalogItemSummaryApiModel} のマッパーです。
 */
public class CatalogItemSummaryMapper {
  /**
   * {@link CatalogItem} オブジェクトを {@link CatalogItemSummaryApiModel} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト。
   * @return {@link CatalogItemSummaryApiModel} オブジェクト。
   */
  public static CatalogItemSummaryApiModel convert(CatalogItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(CatalogItemAsset::getAssetCode).collect(Collectors.toList());

    return new CatalogItemSummaryApiModel(item.getId(), item.getName(), item.getProductCode(),
        assetCodes);
  }
}
