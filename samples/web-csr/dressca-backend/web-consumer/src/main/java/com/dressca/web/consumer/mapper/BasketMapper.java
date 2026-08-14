package com.dressca.web.consumer.mapper;

import com.dressca.applicationmodules.shopping.entity.Basket;
import com.dressca.applicationmodules.shopping.valueobject.Account;
import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import com.dressca.web.consumer.controller.dto.baskets.BasketItemApiModel;
import com.dressca.web.consumer.controller.dto.baskets.GetBasketItemsResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Basket} と {@link GetBasketItemsResponse} のマッパーです。
 */
public class BasketMapper {

  /**
   * {@link Basket} オブジェクトを {@link GetBasketItemsResponse} に変換します。
   * 
   * @param basket {@link Basket} オブジェクト。
   * @return {@link GetBasketItemsResponse} オブジェクト。
   */
  public static GetBasketItemsResponse convert(Basket basket) {
    if (basket == null) {
      return null;
    }

    Account account = basket.getAccount();
    AccountApiModel accountDto =
        new AccountApiModel(Account.CONSUMPTION_TAX_RATE, account.getItemTotalPrice(),
            account.getDeliveryCharge(), account.getConsumptionTax(), account.getTotalPrice());

    List<BasketItemApiModel> basketItems =
        basket.getItems().stream().map(BasketItemMapper::convert).collect(Collectors.toList());

    return new GetBasketItemsResponse(basket.getBuyerId(), accountDto, basketItems,
        Collections.emptyList());
  }
}
