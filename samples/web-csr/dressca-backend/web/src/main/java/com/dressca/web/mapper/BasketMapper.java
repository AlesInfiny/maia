package com.dressca.web.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.accounting.Account;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.web.controller.dto.accounting.AccountResponse;
import com.dressca.web.controller.dto.baskets.BasketItemResponse;
import com.dressca.web.controller.dto.baskets.BasketResponse;

/**
 * {@link Basket} と {@link BasketResponse} のマッパーです。
 */
public class BasketMapper {
  public static BasketResponse convert(Basket basket) {
    if (basket == null) {
      return null;
    }

    Account account = basket.getAccount();
    AccountResponse accountDto =
        new AccountResponse(
            Account.CONSUMPTION_TAX_RATE, 
            account.getItemTotalPrice(),
            account.getDeliveryCharge(), 
            account.getConsumptionTax(), 
            account.getTotalPrice()
        );
    
    List<BasketItemResponse> basketItems = basket.getItems().stream()
        .map(BasketItemMapper::convert)
        .collect(Collectors.toList());
    
    return new BasketResponse(basket.getBuyerId(), accountDto, basketItems);
  }
}
