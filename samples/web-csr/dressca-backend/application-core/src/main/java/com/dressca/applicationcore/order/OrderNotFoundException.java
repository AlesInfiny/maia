package com.dressca.applicationcore.order;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 注文情報が存在しないことを表す例外クラスです。
 */
public class OrderNotFoundException extends LogicException {

  /**
   * 見つからなかった注文 Id と購入者 Id を指定して {@link OrderNotFoundException} クラスの新しいインスタンスを初期化します。
   * 
   * @param cause 原因例外
   * @param orderId 見つからなかった注文 Id.
   * @param buyerId 見つからなかった購入者 Id.
   */
  public OrderNotFoundException(Throwable cause, long orderId, String buyerId) {
    super(cause, ExceptionIdConstant.E_ORDER0002, new String[] {String.valueOf(orderId), buyerId},
        new String[] {String.valueOf(orderId), buyerId});
  }

}
