package com.dressca.cms.systemcommon.exception;

import java.util.Arrays;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.cms.systemcommon.util.ApplicationContextWrapper;
import lombok.Getter;

/**
 * 業務例外を表す例外クラスです。
 */
@Getter
public class LogicException extends Exception {
  private final String exceptionId;
  private final String[] logMessageValue;

  /**
   * 原因例外、例外 ID 、メッセージ用プレースホルダー（ログ用）を指定して、 {@link LogicException}
   * クラスのインスタンスを初期化します。
   * 
   * @param cause 原因例外。
   * @param exceptionId 例外 ID 。
   * @param logMessageValue メッセージ用プレースホルダー（ログ用）。
   */
  public LogicException(Throwable cause, String exceptionId, String[] logMessageValue) {
    super(resolveMessage(exceptionId, logMessageValue), cause);
    this.exceptionId = exceptionId;
    this.logMessageValue =
        logMessageValue == null ? null : Arrays.copyOf(logMessageValue, logMessageValue.length);
  }

  /**
   * 外部参照による内部配列の破壊を防ぐため、配列の複製を返却します。
   *
   * @return メッセージ用プレースホルダー（ログ用）。
   */
  public String[] getLogMessageValue() {
    return this.logMessageValue == null ? null
        : Arrays.copyOf(this.logMessageValue, this.logMessageValue.length);
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
