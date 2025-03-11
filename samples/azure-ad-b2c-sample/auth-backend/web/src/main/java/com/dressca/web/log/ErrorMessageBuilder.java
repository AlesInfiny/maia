package com.dressca.web.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ログでエラーメッセージを作成するためのクラスです。
 */
@Getter
@AllArgsConstructor
public class ErrorMessageBuilder {

  private static final MessageSource messageSource = (MessageSource) ApplicationContextWrapper
      .getBean(MessageSource.class);

  private Exception ex;
  private String exceptionId;
  private String[] logMessageValue;
  private String[] frontMessageValue;

  /**
   * ProblemDetails の detail 情報に格納するスタックトレースを作成します。
   * 
   * @return スタックトレース。
   */
  public String createLogMessageStackTrace() {
    StringBuilder builder = new StringBuilder();
    String exceptionMessage = messageSource.getMessage(exceptionId, logMessageValue, Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage).append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }
}
