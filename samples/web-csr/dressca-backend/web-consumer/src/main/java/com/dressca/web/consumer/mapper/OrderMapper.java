package com.dressca.web.consumer.mapper;

import com.dressca.applicationmodules.shopping.entity.Order;
import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import com.dressca.web.consumer.controller.dto.order.GetOrderByIdResponse;
import java.util.stream.Collectors;

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
        order.getShipToAddress().fullName(), order.getShipToAddress().address().postalCode(),
        order.getShipToAddress().address().todofuken(),
        order.getShipToAddress().address().shikuchoson(),
        order.getShipToAddress().address().azanaAndOthers(),
        new AccountApiModel(order.getConsumptionTaxRate(), order.getTotalItemsPrice(),
            order.getDeliveryCharge(), order.getConsumptionTax(), order.getTotalPrice()),
        order.getOrderItems().stream().map(OrderItemMapper::convert).collect(Collectors.toList()));
  }
}
