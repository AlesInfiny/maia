package com.dressca.cms.systemcommon.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * システム例外を表す例外クラスです。
 */
@Getter
@Setter
public class SystemException extends RuntimeException {
  private String exceptionId = null;

  private String[] logMessageValue = null;

  /**
   * 原因例外、例外 ID 、メッセージ用プレースホルダー（ログ用）を指定して、
   * {@link SystemException} クラスのインスタンスを初期化します。
   * 
   * @param cause           原因例外。
   * @param exceptionId     例外 ID 。
   * @param logMessageValue メッセージ用プレースホルダー（ログ用）。
   */
  public SystemException(Throwable cause, String exceptionId, String[] logMessageValue) {
    super(cause);
    this.exceptionId = exceptionId;
    this.logMessageValue = logMessageValue;
  }

}
