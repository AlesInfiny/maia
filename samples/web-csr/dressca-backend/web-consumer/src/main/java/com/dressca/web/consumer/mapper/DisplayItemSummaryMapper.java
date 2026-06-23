package com.dressca.web.consumer.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayItemAsset;
import com.dressca.web.consumer.controller.dto.displayitem.DisplayItemSummaryApiModel;

/**
 * {@link DisplayItem} と {@link DisplayItemSummaryApiModel} のマッパーです。
 */
public class DisplayItemSummaryMapper {
  /**
   * {@link DisplayItem} オブジェクトを {@link DisplayItemSummaryApiModel} に変換します。
   * 
   * @param item {@link DisplayItem} オブジェクト。
   * @return {@link DisplayItemSummaryApiModel} オブジェクト。
   */
  public static DisplayItemSummaryApiModel convert(DisplayItem item) {
    if (item == null) {
      return null;
    }

    List<String> assetCodes =
        item.getAssets().stream().map(DisplayItemAsset::getAssetCode).collect(Collectors.toList());

    return new DisplayItemSummaryApiModel(item.getId(), item.getName(), item.getProductCode(),
        assetCodes);
  }
}
