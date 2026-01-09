package com.dressca.applicationcore.order;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

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
  public OrderNotFoundException(Throwable cause, long orderId, String buyerId) {
    super(cause, ExceptionIdConstants.E_ORDER_NOT_FOUND,
        new String[] {String.valueOf(orderId), buyerId},
        new String[] {String.valueOf(orderId), buyerId});
  }
}
