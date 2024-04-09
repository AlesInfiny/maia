package com.dressca.web.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.web.constant.ProblemDetailsConstant;
import com.dressca.web.log.ErrorMessageBuilder;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * サーバーエラーのハンドリングを行うクラスです。
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 例外をステータースコード401で返却する。
   *
   * @param e   未認証の例外
   * @param req リクエスト
   * @return ステータースコード401のレスポンス
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> accessDeniedHandleException(AccessDeniedException e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, ExceptionIdConstant.E_AUTH0001, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(null);
  }

  /**
   * 例外をステータースコード500で返却する。
   *
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, ExceptionIdConstant.E_SHARE0000, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.SYSTEM_ERROR_TITLE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  private ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String title) {
    Map<String, String> errorProperty = Map.of(errorBuilder.getExceptionId(), errorBuilder.createFrontErrorMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle(title);
    problemDetail.setProperty(ProblemDetailsConstant.ERROR_KEY, errorProperty);
    return problemDetail;
  }
}

