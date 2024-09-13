package com.dressca.web.admin.mapper;

import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.web.admin.controller.dto.baskets.BasketItemResponse;

/**
 * {@link BasketItem} と {@link BasketItemResponse} のマッパーです。
 */
public class BasketItemMapper {

  /**
   * {@link BasketItem} オブジェクトを {@link BasketItemResponse} に変換します。
   * 
   * @param basketItem {@link BasketItem} オブジェクト
   * @return {@link BasketItemResponse} オブジェクト
   */
  public static BasketItemResponse convert(BasketItem basketItem) {
    if (basketItem == null) {
      return null;
    }

    return new BasketItemResponse(
        basketItem.getCatalogItemId(),
        basketItem.getUnitPrice(),
        basketItem.getQuantity(),
        basketItem.getSubtotal(),
        null);
  }
}
