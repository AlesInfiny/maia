package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryResponse;

/**
 * {@link CatalogItem} と {@link CatalogItemSummaryResponse} のマッパーです。
 */
public class CatalogItemSummaryMapper {
  /**
   * {@link CatalogItem} オブジェクトを {@link CatalogItemSummaryResponse} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト。
   * @return {@link CatalogItemSummaryResponse} オブジェクト。
   */
  public static CatalogItemSummaryResponse convert(CatalogItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(CatalogItemAsset::getAssetCode).collect(Collectors.toList());

    return new CatalogItemSummaryResponse(item.getId(), item.getName(), item.getProductCode(),
        assetCodes);
  }
}
