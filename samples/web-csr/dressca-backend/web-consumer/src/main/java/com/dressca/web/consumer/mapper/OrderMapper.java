package com.dressca.web.consumer.mapper;

import java.util.stream.Collectors;
import com.dressca.applicationcore.order.Order;
import com.dressca.web.consumer.controller.dto.accounting.AccountResponse;
import com.dressca.web.consumer.controller.dto.order.OrderResponse;

/**
 * {@link Order} と {@link OrderResponse} のマッパーです。
 */
public class OrderMapper {

  /**
   * {@link Order} オブジェクトを {@link OrderResponse} に変換します。
   * 
   * @param order {@link Order} オブジェクト。
   * @return {@link OrderResponse} オブジェクト。
   */
  public static OrderResponse convert(Order order) {
    return new OrderResponse(order.getId(), order.getBuyerId(), order.getOrderDate(),
        order.getShipToAddress().getFullName(),
        order.getShipToAddress().getAddress().getPostalCode(),
        order.getShipToAddress().getAddress().getTodofuken(),
        order.getShipToAddress().getAddress().getShikuchoson(),
        order.getShipToAddress().getAddress().getAzanaAndOthers(),
        new AccountResponse(order.getConsumptionTaxRate(), order.getTotalItemsPrice(),
            order.getDeliveryCharge(), order.getConsumptionTax(), order.getTotalPrice()),
        order.getOrderItems().stream().map(OrderItemMapper::convert).collect(Collectors.toList()));
  }
}
