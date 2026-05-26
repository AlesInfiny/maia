package com.dressca.applicationcore.order;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * 注文情報が存在しないことを表す例外クラスです。
 */
public class OrderNotFoundException extends LogicException {

  public OrderNotFoundException(Throwable cause, UUID orderId, UUID buyerId) {
    super(cause, ExceptionIdConstants.E_ORDER_NOT_FOUND,
        new String[] {String.valueOf(orderId), String.valueOf(buyerId)},
        new String[] {String.valueOf(orderId), String.valueOf(buyerId)});
  }
}
