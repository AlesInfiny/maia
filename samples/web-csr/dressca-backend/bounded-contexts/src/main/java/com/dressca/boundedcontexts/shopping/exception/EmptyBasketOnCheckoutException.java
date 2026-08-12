package com.dressca.boundedcontexts.shopping.exception;

import com.dressca.systemcommon.exception.LogicException;
import com.dressca.boundedcontexts.shopping.constant.ShoppingExceptionIdConstants;

/**
 * 注文のチェックアウト処理開始時に買い物かごが空であることを表す例外クラスです。
 */
public class EmptyBasketOnCheckoutException extends LogicException {

  /**
   * 原因例外を指定して、 {@link EmptyBasketOnCheckoutException} クラスのインスタンスを初期化します。
   * 
   * @param cause 原因例外。
   */
  public EmptyBasketOnCheckoutException(Throwable cause) {
    super(cause, ShoppingExceptionIdConstants.E_BASKET_IS_EMPTY_ON_CHECKOUT, null, null);
  }
}
