package com.dressca.web.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import lombok.Getter;
import lombok.Setter;

/**
 * ログやレスポンスでエラーメッセージを作成するためのクラスです。
 */
@Setter
@Getter
public class CreateErrorMessage {

  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String EXCEPTION_MESSAGE_SUFFIX_FRONT = "front";
  private static final String PROPERTY_DELIMITER = ".";

  private Exception ex;
  private String exceptionId;
  private String[] logMessageValue;
  private String[] frontMessageValue;

  /**
   * エラーメッセージ用オブジェクトの生成制御を行うBuilderクラスです。
   */
  public static class ErrorMessageBuilder {

    private Exception ex;
    private String exceptionId;
    private String[] logMessageValue;
    private String[] frontMessageValue;

    /**
     * 例外情報の格納を行うクラス。
     * 
     * @param e                 例外
     * @param exceptionId       例外ID
     * @param logMessageValue   ログ用メッセージの要素
     * @param frontMessageValue フロント用メッセージの要素
     * @return ErrorMessageBuilderインスタンス
     */
    public ErrorMessageBuilder errorMessageBuilder(Exception e, String exceptionId, String[] logMessageValue,
        String[] frontMessageValue) {
      this.ex = e;
      this.exceptionId = exceptionId;
      this.logMessageValue = logMessageValue;
      this.frontMessageValue = frontMessageValue;
      return this;
    }

    /**
     * 格納された例外情報の検証とCreateErrorMessageインスタンスの生成行うクラス。
     * 
     * @return CreateErrorMessageインスタンス
     */
    public CreateErrorMessage build() {
      if ((ex == null) || exceptionId == null) {
        throw new SystemException(ex, ExceptionIdConstant.E_SHARE0000, null, null);
      }
      return new CreateErrorMessage(this);
    }
  }

  private CreateErrorMessage(ErrorMessageBuilder builder) {
    this.ex = builder.ex;
    this.exceptionId = builder.exceptionId;
    this.logMessageValue = builder.logMessageValue;
    this.frontMessageValue = builder.frontMessageValue;
  }

  /**
   * ログに出力するためのメッセージを作成します。
   * 
   * @return 例外のスタックトレース
   */
  public String createLogMessageStackTrace() {
    StringBuilder builder = new StringBuilder();
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    String exceptionMessage = messageSource.getMessage(code, logMessageValue, Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage).append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }

  /**
   * 開発環境用のProblemDetailのエラーメッセージを格納します。
   * 
   * @return ログ用のエラーメッセージ
   */
  public String createLogErrorValue() {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(code, logMessageValue, Locale.getDefault());
  }

  /**
   * 本番環境用のProblemDetailのエラーメッセージを格納します。
   * 
   * @return フロント用のエラーメッセージ
   */
  public String createFrontErrorValue() {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_FRONT);
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(code, frontMessageValue, Locale.getDefault());
  }
}
