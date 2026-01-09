package com.dressca.applicationcore.authorization;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * ユーザーに実行権限がないことを表す例外クラスです。
 */
public class PermissionDeniedException extends LogicException {
  /**
   * 実行を試みた操作を指定して、 {@link PermissionDeniedException} クラスの新しいインスタンスを初期化します。
   * 
   * @param operationName 実行を試みた操作。
   */
  public PermissionDeniedException(String operationName) {
    super(null, ExceptionIdConstants.E_PERMISSION_DENIED,
        new String[] {String.valueOf(operationName)}, new String[] {String.valueOf(operationName)});
  }
}
