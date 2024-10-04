package com.dressca.web.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import com.dressca.systemcommon.constant.CommonExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.constant.ProblemDetailsConstant;
import com.dressca.web.log.ErrorMessageBuilder;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * サーバーエラーのハンドリングを行うクラスです（本番環境用）。
 */
@ControllerAdvice(basePackages = "com.dressca")
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
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, CommonExceptionIdConstant.E_BUSINESS, null, null);
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
  public ResponseEntity<ProblemDetail> handleSystemException(SystemException e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, CommonExceptionIdConstant.E_SYSTEM, null, null);
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
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, CommonExceptionIdConstant.E_SYSTEM, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.SYSTEM_ERROR_TITLE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  private ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String title) {
    Map<String, Object> errorProperty = new LinkedHashMap<String, Object>() {
      {
        put(ProblemDetailsConstant.EXCEPTION_ID, errorBuilder.getExceptionId());
        put(ProblemDetailsConstant.EXCEPTION_VALUES, errorBuilder.getFrontMessageValue());
      }
    };
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle(title);
    problemDetail.setProperties(errorProperty);
    ;
    return problemDetail;
  }
}
