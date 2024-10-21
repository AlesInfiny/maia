package com.dressca.web.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.constant.ProblemDetailsConstant;
import com.dressca.web.log.ErrorMessageBuilder;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.annotation.Profile;

/**
 * サーバーエラーのハンドリングを行うクラスです（本番環境用）。
 */
@ControllerAdvice
@Profile("production | test")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 未認証エラーをステータスコード401で返却する。
   *
   * @param e   未認証エラー
   * @param req リクエスト
   * @return ステータースコード401のレスポンス
   */
  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(
      AuthenticationCredentialsNotFoundException e, HttpServletRequest req) {
    apLog.error(ExceptionUtils.getStackTrace(e));
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * 認可エラーをステータスコード404で返却する。
   *
   * @param e   認可エラー
   * @param req リクエスト
   * @return ステータースコード404のレスポンス
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<String> handleAuthorizationDeniedException(
      AuthorizationDeniedException e, HttpServletRequest req) {
    apLog.error(ExceptionUtils.getStackTrace(e));
    return ResponseEntity.notFound().build();
  }

  /**
   * その他の業務エラーをステータースコード500で返却する（本番環境、テスト環境用）。
   *
   * @param e   業務例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ProblemDetail> handleLogicException(LogicException e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(), e.getLogMessageValue(),
        e.getFrontMessageValue());
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.LOGIC_ERROR_TITLE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  /**
   * その他のシステムエラーをステータースコード500で返却する（本番環境、テスト環境用）。
   *
   * @param e   その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ProblemDetail> handleException(SystemException e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(), e.getLogMessageValue(),
        e.getFrontMessageValue());
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.SYSTEM_ERROR_TITLE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  /**
   * 上記のいずれにも当てはまらない例外をステータースコード500で返却する（本番環境、テスト環境用）。
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
