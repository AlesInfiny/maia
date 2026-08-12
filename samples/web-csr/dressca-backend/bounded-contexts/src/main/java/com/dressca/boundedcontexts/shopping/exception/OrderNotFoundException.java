package com.dressca.boundedcontexts.shopping.exception;

import com.dressca.systemcommon.exception.LogicException;
import com.dressca.boundedcontexts.shopping.constant.ShoppingExceptionIdConstants;
import java.util.UUID;

/**
 * 注文情報が存在しないことを表す例外クラスです。
 */
public class OrderNotFoundException extends LogicException {

  /**
   * 原因例外、見つからなかった注文 ID 、購入者 ID を指定して、 {@link OrderNotFoundException} クラスの新しいインスタンスを初期化します。
   * 
   * @param cause 原因例外。
   * @param orderId 見つからなかった注文 ID 。
   * @param buyerId 見つからなかった購入者 ID 。
   */
  public OrderNotFoundException(Throwable cause, UUID orderId, UUID buyerId) {
    super(cause, ShoppingExceptionIdConstants.E_ORDER_NOT_FOUND,
        new String[] {String.valueOf(orderId), String.valueOf(buyerId)},
        new String[] {String.valueOf(orderId), String.valueOf(buyerId)});
  }
}
