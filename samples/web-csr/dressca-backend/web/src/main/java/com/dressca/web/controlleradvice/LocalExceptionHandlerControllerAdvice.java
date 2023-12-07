package com.dressca.web.controlleradvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import com.dressca.systemcommon.constant.ProblemDetailConstant;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Profile("local")
public class LocalExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String PROPERTY_DELIMITER = ".";

  /**
   * その他の業務エラーをステータースコード500で返却する（開発環境用）。
   * 
   * @param e   業務例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ProblemDetail> localHandleLogicException(LogicException e, HttpServletRequest req) {
    apLog.error(createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    ProblemDetail problemDetail = createLogicProblemDetail(e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  /**
   * その他のシステムエラーをステータースコード500で返却する（開発環境用）。
   * 
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ProblemDetail> localHandleException(SystemException e, HttpServletRequest req) {
    apLog.error(createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    ProblemDetail problemDetail = createSystemProblemDetail(e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  /**
   * 上記のいずれにも当てはまらない例外をステータースコード500で返却する（開発環境用）。
   * 
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> localHandleException(Exception e, HttpServletRequest req) {
    apLog.error(createLogMessageStackTrace(e, ExceptionIdConstant.E_SHARE0000, null));
    ProblemDetail problemDetail = createSystemProblemDetail(
        new SystemException(e, ExceptionIdConstant.E_SHARE0000, null, null));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  private ProblemDetail createLogicProblemDetail(LogicException e) {
    Map<String, String> errorProperty = Map.of(e.getExceptionId(),
        createErrorValue(e.getExceptionId(), e.getLogMessageValue()));
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    problemDetail.setTitle(ProblemDetailConstant.LOGIC_ERROR_TITLE);
    problemDetail.setProperty(ProblemDetailConstant.ERROR_KEY, errorProperty);

    return problemDetail;
  }

  private ProblemDetail createSystemProblemDetail(SystemException e) {
    Map<String, String> errorProperty = Map.of(e.getExceptionId(),
        createErrorValue(e.getExceptionId(), e.getLogMessageValue()));
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    problemDetail.setTitle(ProblemDetailConstant.SYSTEM_ERROR_TITLE);
    problemDetail.setProperty(ProblemDetailConstant.ERROR_KEY, errorProperty);

    return problemDetail;
  }

  private String createLogMessageStackTrace(Exception e, String exceptionId, String[] logMessageValue) {
    StringBuilder builder = new StringBuilder();
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    String exceptionMessage = messageSource.getMessage(code, logMessageValue, Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage)
        .append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    e.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }

  private String createErrorValue(String exceptionId, String[] logMessageValue) {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(code, logMessageValue, Locale.getDefault());
  }
}
