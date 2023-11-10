package com.dressca.web.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.exception.response.ErrorResponse;
import com.dressca.systemcommon.util.ApplicationContextWrapper;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.MessageSource;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger aplog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String PROPERTY_DELIMITER = ".";

  /**
   * その他の業務エラーをステータースコード500で返却する。
   * 
   * @param e   業務例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ErrorResponse> handleLogicException(LogicException e, HttpServletRequest req) {
    aplog.error(createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(createErrorResponse(e, req));
  }

  /**
   * その他のシステムエラーをステータースコード500で返却する。
   * 
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ErrorResponse> handleException(SystemException e, HttpServletRequest req) {
    aplog.error(createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(createErrorResponse(e, req));
  }

  /**
   * 上記のいずれにも当てはまらない例外をステータースコード500で返却する。
   * 
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest req) {
    aplog.error(createLogMessageStackTrace(e, ExceptionIdConstant.E_SHARE0000, null));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(createErrorResponse(
            new SystemException(e, ExceptionIdConstant.E_SHARE0000, null, null), req));
  }

  private ErrorResponse createErrorResponse(LogicException e, HttpServletRequest request) {
    String urlPath = request.getPathInfo();
    if (StringUtils.isEmpty(urlPath)) {
      urlPath = request.getServletPath();
    }
    return new ErrorResponse(e, urlPath);
  }

  private ErrorResponse createErrorResponse(SystemException e, HttpServletRequest request) {
    String urlPath = request.getPathInfo();
    if (StringUtils.isEmpty(urlPath)) {
      urlPath = request.getServletPath();
    }
    return new ErrorResponse(e, urlPath);
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

}
