package com.dressca.web.controlleradvice;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dressca.web.constant.ProblemDetailsConstant;
import com.dressca.web.log.ErrorMessageBuilder;
import jakarta.servlet.http.HttpServletRequest;

/**
 * サーバーエラーのハンドリングを行うクラスです（開発環境用）。
 */
@ControllerAdvice
@Profile("local")
public class LocalExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * その他の業務エラーをステータースコード500で返却する（開発環境用）。
   * 
   * @param e   業務例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ProblemDetail> localHandleLogicException(LogicException e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(), e.getLogMessageValue(),
        e.getFrontMessageValue());
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.LOGIC_ERROR_TITLE);
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
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(), e.getLogMessageValue(),
        e.getFrontMessageValue());
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.SYSTEM_ERROR_TITLE);
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
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, ExceptionIdConstant.E_SHARE0000, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = createProblemDetail(errorBuilder, ProblemDetailsConstant.SYSTEM_ERROR_TITLE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(problemDetail);
  }

  private ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String title) {
    Map<String, String> errorProperty = Map.of(errorBuilder.getExceptionId(), errorBuilder.createFrontErrorMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        errorBuilder.createLogMessageStackTrace());
    problemDetail.setTitle(title);
    problemDetail.setProperty(ProblemDetailsConstant.ERROR_KEY, errorProperty);
    return problemDetail;
  }
}
