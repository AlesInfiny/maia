package com.dressca.web.mapper;

import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.web.controller.dto.baskets.BasketItemResponse;

/**
 * {@link BasketItem} と {@link BasketItemResponse} のマッパーです。
 */
public class BasketItemMapper {
  public static BasketItemResponse convert(BasketItem basketItem) {
    if (basketItem == null) {
      return null;
    }

    return new BasketItemResponse(
        basketItem.getCatalogItemId(), 
        basketItem.getUnitPrice(),
        basketItem.getQuantity(), 
        basketItem.getSubtotal(), 
        null
    );
  }
}
