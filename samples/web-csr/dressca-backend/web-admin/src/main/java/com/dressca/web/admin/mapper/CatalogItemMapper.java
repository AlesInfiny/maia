package com.dressca.web.admin.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.web.admin.controller.dto.catalog.CatalogItemResponse;

/**
 * {@link CatalogItem} と {@link CatalogItemResponse} のマッパーです。
 */
public class CatalogItemMapper {
  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * {@link CatalogItem} オブジェクトを {@link CatalogItemResponse} に変換します。
   * 
   * @param item {@link CatalogItem} オブジェクト
   * @return {@link CatalogItemResponse} オブジェクト
   */
  public static CatalogItemResponse convert(CatalogItem item) {

    if (item == null) {
      return null;
    }

    List<String> assetCodes = item.getAssets().stream()
        .map(CatalogItemAsset::getAssetCode)
        .collect(Collectors.toList());

    apLog.info(assetCodes.toString());
    return new CatalogItemResponse(
        item.getId(),
        item.getName(),
        item.getProductCode(),
        assetCodes,
        item.getDescription(),
        item.getPrice(),
        item.getCatalogCategoryId(),
        item.getCatalogBrandId(),
        item.getRowVersion());
  }
}
