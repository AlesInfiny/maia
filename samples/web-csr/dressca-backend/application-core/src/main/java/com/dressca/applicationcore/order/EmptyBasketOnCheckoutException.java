package com.dressca.applicationcore.order;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 注文のチェックアウト処理開始時に買い物かごが空であることを表す例外クラスです。
 */
public class EmptyBasketOnCheckoutException extends LogicException {

  public EmptyBasketOnCheckoutException(Throwable cause) {
    super(cause, ExceptionIdConstant.E_ORDER0001, null, null);
  }

}
