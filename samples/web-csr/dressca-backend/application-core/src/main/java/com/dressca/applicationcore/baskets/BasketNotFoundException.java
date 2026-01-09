package com.dressca.applicationcore.baskets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かごが存在しないことを表す例外クラスです。
 */
public class BasketNotFoundException extends LogicException {

  /**
   * 存在しない買い物かご ID を指定して、 {@link BasketNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param basketId 見つからなかった買い物かご ID 。
   */
  public BasketNotFoundException(long basketId) {
    super(null, ExceptionIdConstants.E_BASKET_IS_NULL_ON_CHECKOUT,
        new String[] {String.valueOf(basketId)}, new String[] {String.valueOf(basketId)});
  }
}
