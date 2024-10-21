package com.dressca.systemcommon.exception;

import com.dressca.systemcommon.constant.ExceptionIdConstant;

/**
 * 楽観ロックエラーが発生したことを表す例外です。
 */
public class OptimisticLockingFailureException extends LogicException {

  /**
   * カタログアイテムidを指定して、
   * {@link OptimisticLockingFailureException} クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogItemId 更新処理を試みたカタログアイテムid
   */
  public OptimisticLockingFailureException(long catalogItemId) {
    super(
        null,
        ExceptionIdConstant.E_CATALOG0005,
        new String[] { String.valueOf(catalogItemId) },
        new String[] { String.valueOf(catalogItemId) });
  }

}
