package com.dressca.web.consumer.mapper;

import com.dressca.domainmodules.shopping.models.OrderItem;
import com.dressca.domainmodules.shopping.models.OrderItemAsset;
import com.dressca.web.consumer.controller.dto.displayitem.DisplayItemSummaryApiModel;
import com.dressca.web.consumer.controller.dto.order.OrderItemApiModel;
import java.util.stream.Collectors;

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
        new DisplayItemSummaryApiModel(item.getItemOrdered().getDisplayItemId(),
            item.getItemOrdered().getProductName(), item.getItemOrdered().getProductCode(),
            item.getAssets().stream().map(OrderItemAsset::getAssetCode)
                .collect(Collectors.toList())),
        item.getQuantity(), item.getUnitPrice(), item.getSubTotal());
  }
}
