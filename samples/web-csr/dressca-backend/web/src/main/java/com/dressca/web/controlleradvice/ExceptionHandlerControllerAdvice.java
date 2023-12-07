package com.dressca.web.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.ProblemDetailConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.log.CreateErrorMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.annotation.Profile;

/**
 * サーバーエラーのハンドリングを行うクラスです。
 */
@ControllerAdvice
@Profile("production | test")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * その他の業務エラーをステータースコード500で返却する（本番環境、テスト環境用）。
   *
   * @param e   業務例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ProblemDetail> handleLogicException(LogicException e, HttpServletRequest req) {
    apLog.error(CreateErrorMessage.createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    Map<String, String> errorProperty = Map.of(e.getExceptionId(),
        CreateErrorMessage.createFrontErrorValue(e.getExceptionId(), e.getFrontMessageValue()));
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle(ProblemDetailConstant.LOGIC_ERROR_TITLE);
    problemDetail.setProperty(ProblemDetailConstant.ERROR_KEY, errorProperty);
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
    apLog.error(CreateErrorMessage.createLogMessageStackTrace(e, e.getExceptionId(), e.getLogMessageValue()));
    Map<String, String> errorProperty = Map.of(e.getExceptionId(),
        CreateErrorMessage.createFrontErrorValue(e.getExceptionId(), e.getFrontMessageValue()));
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle(ProblemDetailConstant.SYSTEM_ERROR_TITLE);
    problemDetail.setProperty(ProblemDetailConstant.ERROR_KEY, errorProperty);
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
    apLog.error(CreateErrorMessage.createLogMessageStackTrace(e, ExceptionIdConstant.E_SHARE0000,
        null));
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle(ProblemDetailConstant.SYSTEM_ERROR_TITLE);
    problemDetail.setProperty(ExceptionIdConstant.E_SHARE0000,
        CreateErrorMessage.createFrontErrorValue(ExceptionIdConstant.E_SHARE0000, null));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }
}
