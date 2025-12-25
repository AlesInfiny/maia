package com.dressca.web.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import com.dressca.web.log.ErrorMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private AbstractStructuredLogger apLog;

  @Autowired
  private ProblemDetailsFactory problemDetailsFactory;

  /**
   * 未認証の例外をステータースコード 401 で返却します。
   *
   * @param e 未認証の例外。
   * @param req リクエスト。
   * @return ステータースコード 401 のレスポンス。
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ProblemDetail> accessDeniedHandleException(AccessDeniedException e,
      HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder =
        new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_UNAUTHORIZED, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
        CommonExceptionIdConstants.E_BUSINESS,
        HttpStatus.UNAUTHORIZED);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }

  /**
   * その他の例外をステータースコード 500 で返却します。
   *
   * @param e その他の例外。
   * @param req リクエスト。
   * @return ステータースコード 500 のレスポンス。
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder =
        new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
        CommonExceptionIdConstants.E_SYSTEM,
        HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }
}
