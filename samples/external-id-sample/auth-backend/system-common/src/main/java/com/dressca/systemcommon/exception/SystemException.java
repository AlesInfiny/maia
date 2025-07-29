package com.dressca.systemcommon.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * システム例外を表す例外クラスです。
 */
@Getter
@Setter
public class SystemException extends RuntimeException {

  private String exceptionId = null;

  private String[] frontMessageValue = null;

  private String[] logMessageValue = null;

  /**
   * {@link SystemException} クラスのインスタンスを初期化します。
   *
   * @param cause             原因例外。
   * @param exceptionId       例外 ID 。
   * @param frontMessageValue メッセージ用プレースフォルダ（フロント用）。
   * @param logMessageValue   メッセージ用プレースフォルダ（ログ用）。
   */
  public SystemException(Throwable cause, String exceptionId,
      String[] frontMessageValue, String[] logMessageValue) {
    super(cause);
    this.exceptionId = exceptionId;
    this.frontMessageValue = frontMessageValue;
    this.logMessageValue = logMessageValue;
  }
}
