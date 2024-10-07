package com.dressca.applicationcore.authorization;

import com.dressca.systemcommon.exception.LogicException;

/**
 * ユーザーに実行権限がないことを表す例外です。
 */
public class PermissionDeniedException extends LogicException {
  public PermissionDeniedException(String operationName) {
    super(
        null,
        "{0} を実行する権限がありません。",
        new String[] { String.valueOf(operationName) },
        new String[] { String.valueOf(operationName) });
  }
}