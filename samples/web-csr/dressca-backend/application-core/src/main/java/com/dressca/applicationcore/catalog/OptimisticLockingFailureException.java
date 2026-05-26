package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * 楽観ロックエラーを表す例外です。
 */
public class OptimisticLockingFailureException extends LogicException {

  public OptimisticLockingFailureException(UUID catalogItemId, String operationName) {
    super(null, ExceptionIdConstants.E_OPTIMISTIC_LOCKING_FAILURE,
        new String[] {String.valueOf(catalogItemId), operationName},
        new String[] {String.valueOf(catalogItemId), operationName});
  }
}
