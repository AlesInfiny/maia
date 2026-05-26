package com.dressca.applicationcore.baskets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * 買い物かごが存在しないことを表す例外です。
 */
public class BasketNotFoundException extends LogicException {

  public BasketNotFoundException(UUID basketId) {
    super(null, ExceptionIdConstants.E_BASKET_IS_NULL_ON_CHECKOUT,
        new String[] {String.valueOf(basketId)}, new String[] {String.valueOf(basketId)});
  }
}
