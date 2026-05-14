package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.web.consumer.controller.dto.baskets.BasketItemApiModel;

/**
 * {@link BasketItem} と {@link BasketItemApiModel} のマッパーです。
 */
public class BasketItemMapper {

  /**
   * {@link BasketItem} オブジェクトを {@link BasketItemApiModel} に変換します。
   * 
   * @param basketItem {@link BasketItem} オブジェクト。
   * @return {@link BasketItemApiModel} オブジェクト。
   */
  public static BasketItemApiModel convert(BasketItem basketItem) {
    if (basketItem == null) {
      return null;
    }

    return new BasketItemApiModel(basketItem.getCatalogItemId(), basketItem.getUnitPrice(),
        basketItem.getQuantity(), basketItem.getSubtotal(), null);
  }
}
