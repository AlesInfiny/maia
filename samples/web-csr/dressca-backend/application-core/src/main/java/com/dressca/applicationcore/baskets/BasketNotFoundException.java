package com.dressca.applicationcore.baskets;

import com.dressca.applicationcore.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かごが存在しないことを表す例外クラスです。
 */
public class BasketNotFoundException extends LogicException {
  public BasketNotFoundException(long basketId) {
    super(null, ExceptionIdConstant.E_BASKET_IS_NULL_ON_CHECKOUT, new String[] { String.valueOf(basketId) },
        new String[] { String.valueOf(basketId) });
  }
}
