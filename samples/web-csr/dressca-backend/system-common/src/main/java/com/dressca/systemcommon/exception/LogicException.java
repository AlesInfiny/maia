package com.dressca.systemcommon.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 業務例外を表す例外クラスです。
 */
@Getter
@Setter
public class LogicException extends Exception {

  private String exceptionId = null;

  private String[] frontMessageValue = null;

  private String[] logMessageValue = null;

  /**
   * コンストラクタ。
   *
   * @param cause             原因例外
   * @param exceptionId       例外ID
   * @param frontMessageValue メッセージ用プレースフォルダ（フロント用）
   * @param logMessageValue   メッセージ用プレースフォルダ（ログ用）
   */
  public LogicException(Throwable cause, String exceptionId,
      String[] frontMessageValue, String[] logMessageValue) {
    super(cause);
    this.exceptionId = exceptionId;
    this.frontMessageValue = frontMessageValue;
    this.logMessageValue = logMessageValue;
  }
}
