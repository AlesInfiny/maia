package com.dressca.modules.catalog;

import com.dressca.modules.common.exception.LogicException;
import com.dressca.modules.constants.ExceptionIdConstants;

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
