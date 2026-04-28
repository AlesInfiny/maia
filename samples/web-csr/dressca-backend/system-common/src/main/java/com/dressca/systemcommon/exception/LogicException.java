package com.dressca.systemcommon.exception;

import java.util.Arrays;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
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
   * 原因例外、例外 ID 、メッセージ用プレースフォルダ（フロント用）、メッセージ用プレースフォルダ（ログ用）を指定して、 {@link LogicException}
   * クラスのインスタンスを初期化します。
   *
   * @param cause 原因例外。
   * @param exceptionId 例外 ID 。
   * @param frontMessageValue メッセージ用プレースフォルダ（フロント用）。
   * @param logMessageValue メッセージ用プレースフォルダ（ログ用）。
   */
  public LogicException(Throwable cause, String exceptionId, String[] frontMessageValue,
      String[] logMessageValue) {
    super(resolveMessage(exceptionId, logMessageValue), cause);
    this.exceptionId = exceptionId;
    this.frontMessageValue = frontMessageValue == null ? null
        : Arrays.copyOf(frontMessageValue, frontMessageValue.length);
    this.logMessageValue =
        logMessageValue == null ? null : Arrays.copyOf(logMessageValue, logMessageValue.length);
  }

  /**
   * メッセージソースからメッセージを取得します。
   * 
   * @param exceptionId 例外 ID 。
   * @param logMessageValue メッセージ用プレースホルダー（ログ用）。
   * @return 取得したメッセージ。
   */
  private static String resolveMessage(String exceptionId, String[] logMessageValue) {
    MessageSource messages = ApplicationContextWrapper.getBean(MessageSource.class);
    return messages.getMessage(exceptionId, logMessageValue, Locale.getDefault());
  }
}
