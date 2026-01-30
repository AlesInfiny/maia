package com.dressca.cms.systemcommon.exception;

import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.cms.systemcommon.util.ApplicationContextWrapper;
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
   * 原因例外、例外 ID 、メッセージ用プレースホルダー（ログ用）を指定して、 {@link SystemException} クラスのインスタンスを初期化します。
   * 
   * @param cause 原因例外。
   * @param exceptionId 例外 ID 。
   * @param logMessageValue メッセージ用プレースホルダー（ログ用）。
   */
  public SystemException(Throwable cause, String exceptionId, String[] logMessageValue) {
    super(resolveMessage(exceptionId, logMessageValue), cause);
    this.exceptionId = exceptionId;
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
