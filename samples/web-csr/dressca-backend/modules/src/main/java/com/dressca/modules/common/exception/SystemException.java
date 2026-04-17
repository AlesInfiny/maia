package com.dressca.modules.common.exception;

import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.modules.common.util.ApplicationContextWrapper;
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
   * 原因例外、例外 ID 、メッセージ用プレースフォルダ（フロント用）、メッセージ用プレースフォルダ（ログ用）を指定して、 {@link SystemException}
   * クラスのインスタンスを初期化します。
   *
   * @param cause 原因例外。
   * @param exceptionId 例外 ID 。
   * @param frontMessageValue メッセージ用プレースフォルダ（フロント用）。
   * @param logMessageValue メッセージ用プレースフォルダ（ログ用）。
   */
  public SystemException(Throwable cause, String exceptionId, String[] frontMessageValue,
      String[] logMessageValue) {
    super(resolveMessage(exceptionId, logMessageValue), cause);
    this.exceptionId = exceptionId;
    this.frontMessageValue = frontMessageValue;
    this.logMessageValue = logMessageValue;
  }

  /**
   * メッセージソースからメッセージを取得します。
   * 
   * @param exceptionId 例外 ID 。
   * @param logMessageValue メッセージ用プレースホルダー（ログ用）。
   * @return 取得したメッセージ。
   */
  private static String resolveMessage(String exceptionId, String[] logMessageValue) {
    MessageSource messageSource = ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(exceptionId, logMessageValue, Locale.getDefault());
  }
}
