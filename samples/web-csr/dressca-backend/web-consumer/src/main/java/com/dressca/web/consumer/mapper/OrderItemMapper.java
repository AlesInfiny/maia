package com.dressca.web.consumer.mapper;

import com.dressca.applicationmodules.shopping.entity.OrderItem;
import com.dressca.applicationmodules.shopping.entity.OrderItemAsset;
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
        new DisplayItemSummaryApiModel(item.getItemOrdered().displayItemId(),
            item.getItemOrdered().productName(), item.getItemOrdered().productCode(),
            item.getAssets().stream().map(OrderItemAsset::getAssetCode)
                .collect(Collectors.toList())),
        item.getQuantity(), item.getUnitPrice(), item.getSubTotal());
  }
}
