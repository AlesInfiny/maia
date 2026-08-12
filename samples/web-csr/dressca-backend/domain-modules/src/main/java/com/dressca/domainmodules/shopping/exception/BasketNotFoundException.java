package com.dressca.domainmodules.shopping.exception;

import com.dressca.systemcommon.exception.LogicException;
import com.dressca.domainmodules.shopping.constant.ShoppingExceptionIdConstants;
import java.util.UUID;

/**
 * 買い物かごが存在しないことを表す例外クラスです。
 */
public class BasketNotFoundException extends LogicException {

  /**
   * 存在しない買い物かご ID を指定して、 {@link BasketNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param basketId 見つからなかった買い物かご ID 。
   */
  public BasketNotFoundException(UUID basketId) {
    super(null, ShoppingExceptionIdConstants.E_BASKET_IS_NULL_ON_CHECKOUT,
        new String[] {String.valueOf(basketId)}, new String[] {String.valueOf(basketId)});
  }
}
