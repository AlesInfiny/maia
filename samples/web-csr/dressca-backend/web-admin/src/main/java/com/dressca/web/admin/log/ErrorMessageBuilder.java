package com.dressca.web.admin.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ログやレスポンスでエラーメッセージを作成するためのクラスです。
 */
@Getter
@AllArgsConstructor
public class ErrorMessageBuilder {

  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String EXCEPTION_MESSAGE_SUFFIX_FRONT = "front";
  private static final String PROPERTY_DELIMITER = ".";
  private static final MessageSource messageSource = (MessageSource) ApplicationContextWrapper
      .getBean(MessageSource.class);

  private Exception ex;
  private String exceptionId;
  private String[] logMessageValue;
  private String[] frontMessageValue;

  /**
   * ProblemDetailsのdetail情報に格納するスタックトレースを作成します。
   * 
   * @return スタックトレース
   */
  public String createLogMessageStackTrace() {
    StringBuilder builder = new StringBuilder();
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    String exceptionMessage = messageSource.getMessage(code, logMessageValue, Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage).append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }

  /**
   * ProblemDetailsのerror情報に格納するメッセージを作成します。
   * 
   * @return エラーメッセージ
   */
  public String createFrontErrorMessage() {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_FRONT);
    return messageSource.getMessage(code, frontMessageValue, Locale.getDefault());
  }
}
