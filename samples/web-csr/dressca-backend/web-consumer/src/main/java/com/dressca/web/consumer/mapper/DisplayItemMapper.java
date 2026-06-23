package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayItemAsset;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemResponse;

/**
 * {@link DisplayItem} と {@link GetDisplayItemResponse} のマッパーです。
 */
public class DisplayItemMapper {

  /**
   * {@link DisplayItem} オブジェクトを {@link GetDisplayItemResponse} に変換します。
   * 
   * @param item {@link DisplayItem} オブジェクト。
   * @return {@link GetDisplayItemResponse} オブジェクト。
   */
  public static GetDisplayItemResponse convert(DisplayItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(DisplayItemAsset::getAssetCode).collect(Collectors.toList());

    return new GetDisplayItemResponse(item.getId(), item.getCatalogItemId(), item.getName(),
        item.getProductCode(), assetCodes, item.getDescription(), item.getPrice(),
        item.getDisplayCategoryId(), item.getDisplayBrandId());
  }
}
