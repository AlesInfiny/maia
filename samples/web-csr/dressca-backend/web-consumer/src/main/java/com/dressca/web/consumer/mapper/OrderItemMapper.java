package com.dressca.web.consumer.mapper;

import java.util.stream.Collectors;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryApiModel;
import com.dressca.web.consumer.controller.dto.order.OrderItemApiModel;

/**
 * {@link OrderItem} と {@link OrderItemApiModel} のマッパーです。
 */
public class OrderItemMapper {

  /**
   * {@link OrderItem} オブジェクトを {@link OrderItemApiModel} に変換します。
   * 
   * @param item {@link OrderItem} オブジェクト。
   * @return {@link OrderItemApiModel} オブジェクト。
   */
  public static OrderItemApiModel convert(OrderItem item) {
    return new OrderItemApiModel(item.getId(),
        new CatalogItemSummaryApiModel(item.getItemOrdered().getCatalogItemId(),
            item.getItemOrdered().getProductName(), item.getItemOrdered().getProductCode(),
            item.getAssets().stream().map(OrderItemAsset::getAssetCode)
                .collect(Collectors.toList())),
        item.getQuantity(), item.getUnitPrice(), item.getSubTotal());
  }
}
