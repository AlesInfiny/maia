package com.dressca.web.consumer.mapper;

import java.util.stream.Collectors;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryResponse;
import com.dressca.web.consumer.controller.dto.order.OrderItemResponse;

/**
 * {@link OrderItem} と {@link OrderItemResponse} のマッパーです。
 */
public class OrderItemMapper {

  /**
   * {@link OrderItem} オブジェクトを {@link OrderItemResponse} に変換します。
   * 
   * @param item {@link OrderItem} オブジェクト。
   * @return {@link OrderItemResponse} オブジェクト。
   */
  public static OrderItemResponse convert(OrderItem item) {
    return new OrderItemResponse(item.getId(),
        new CatalogItemSummaryResponse(item.getItemOrdered().getCatalogItemId(),
            item.getItemOrdered().getProductName(), item.getItemOrdered().getProductCode(),
            item.getAssets().stream().map(OrderItemAsset::getAssetCode)
                .collect(Collectors.toList())),
        item.getQuantity(), item.getUnitPrice(), item.getSubTotal());
  }
}
