package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 楽観ロックエラーが発生したことを表す例外です。
 */
public class OptimisticLockingFailureException extends LogicException {

  /**
   * カタログアイテム ID を指定して、 {@link OptimisticLockingFailureException} クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogItemId 更新処理を試みたカタログアイテム ID 。
   */
  public OptimisticLockingFailureException(long catalogItemId, String operationName) {
    super(null, ExceptionIdConstants.E_OPTIMISTIC_LOCKING_FAILURE,
        new String[] {String.valueOf(catalogItemId), operationName},
        new String[] {String.valueOf(catalogItemId), operationName});
  }
}
