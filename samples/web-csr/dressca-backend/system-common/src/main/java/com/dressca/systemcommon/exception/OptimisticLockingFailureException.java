package com.dressca.systemcommon.exception;

/**
 * 楽観ロック違反をした場合の例外です。
 */
public class OptimisticLockingFailureException extends LogicException {

  public OptimisticLockingFailureException(Throwable cause, String exceptionId, String[] frontMessageValue,
      String[] logMessageValue) {
    super(cause, exceptionId, frontMessageValue, logMessageValue);
  }

}
