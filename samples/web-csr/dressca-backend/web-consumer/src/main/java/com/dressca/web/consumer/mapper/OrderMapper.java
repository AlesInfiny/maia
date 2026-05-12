package com.dressca.web.consumer.mapper;

import java.util.stream.Collectors;
import com.dressca.applicationcore.order.Order;
import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import com.dressca.web.consumer.controller.dto.order.GetOrderByIdResponse;

/**
 * {@link Order} と {@link GetOrderByIdResponse} のマッパーです。
 */
public class OrderMapper {

  /**
   * {@link Order} オブジェクトを {@link GetOrderByIdResponse} に変換します。
   * 
   * @param order {@link Order} オブジェクト。
   * @return {@link GetOrderByIdResponse} オブジェクト。
   */
  public static GetOrderByIdResponse convert(Order order) {
    return new GetOrderByIdResponse(order.getId(), order.getBuyerId(), order.getOrderDate(),
        order.getShipToAddress().getFullName(),
        order.getShipToAddress().getAddress().getPostalCode(),
        order.getShipToAddress().getAddress().getTodofuken(),
        order.getShipToAddress().getAddress().getShikuchoson(),
        order.getShipToAddress().getAddress().getAzanaAndOthers(),
        new AccountApiModel(order.getConsumptionTaxRate(), order.getTotalItemsPrice(),
            order.getDeliveryCharge(), order.getConsumptionTax(), order.getTotalPrice()),
        order.getOrderItems().stream().map(OrderItemMapper::convert).collect(Collectors.toList()));
  }
}
