package com.dressca.web.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.catalog.OptimisticLockingFailureException;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import com.dressca.web.log.ErrorMessageBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * サーバーエラーのハンドリングを行うクラスです。
 */
@ControllerAdvice(basePackages = "com.dressca")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  @Autowired
  private AbstractStructuredLogger apLog;

  @Autowired
  private ProblemDetailsFactory problemDetailsFactory;

  /**
   * 未認証エラーをステータスコード 401 で返却します。
   *
   * @param e 未認証エラー。
   * @param req リクエスト。
   * @return ステータースコード 401 のレスポンス。
   */
  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(
      AuthenticationCredentialsNotFoundException e, HttpServletRequest req) {
    apLog.warn(ExceptionUtils.getStackTrace(e));
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * 認可エラーをステータスコード 404 で返却します。
   *
   * @param e 認可エラー。
   * @param req リクエスト。
   * @return ステータースコード 404 のレスポンス。
   */
  @ExceptionHandler({AuthorizationDeniedException.class, PermissionDeniedException.class})
  public ResponseEntity<String> handleAuthorizationDeniedException(
      AuthorizationDeniedException e, HttpServletRequest req) {
    apLog.warn(ExceptionUtils.getStackTrace(e));
    return ResponseEntity.notFound().build();
  }

  /**
   * 楽観ロックエラーをステータスコード 409 で返却します。
   * 
   * @param e 楽観ロックエラー。
   * @param req リクエスト。
   * @return ステータスコード 409 のレスポンス。
   */
  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<String> handleOptimisticLockingFailureException(
      OptimisticLockingFailureException e, HttpServletRequest req) {
    apLog.warn(ExceptionUtils.getStackTrace(e));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
  }

  /**
   * その他の業務エラーをステータースコード 500 で返却します。
   *
   * @param e 業務例外。
   * @param req リクエスト。
   * @return ステータースコード 500 のレスポンス。
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ProblemDetail> handleLogicException(LogicException e,
      HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder =
        new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_BUSINESS, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(
        errorBuilder,
        CommonExceptionIdConstants.E_BUSINESS,
        HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }

  /**
   * その他のシステムエラーをステータースコード 500 で返却します。
   *
   * @param e その他の例外。
   * @param req リクエスト。
   * @return ステータースコード 500 のレスポンス。
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ProblemDetail> handleSystemException(SystemException e,
      HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder =
        new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(
        errorBuilder,
        CommonExceptionIdConstants.E_SYSTEM,
        HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }

  /**
   * 上記のいずれにも当てはまらない例外をステータースコード 500 で返却します。
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
