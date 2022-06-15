package com.dressca.systemcommon.exception.controlleradvice;

import javax.servlet.http.HttpServletRequest;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.exception.response.ErrorResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
  
  /**
   * その他の業務エラーをステータースコード422で返却する。
   * 
   * @param e 業務例外
   * @param req リクエスト
   * @return ステータースコード422のレスポンス
   */
  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ErrorResponse> 
      handleLogicException(LogicException e, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
      .contentType(MediaType.APPLICATION_JSON)
      .body(createErrorResponse(e, req));
  }

  /**
   * その他のシステムエラーをステータースコード500で返却する。
   * 
   * @param e その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ErrorResponse> handleException(SystemException e, HttpServletRequest req) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .contentType(MediaType.APPLICATION_JSON)
      .body(createErrorResponse(e, req));
  }

  /**
   * 上記のいずれにも当てはまらない例外をステータースコード500で返却する。
   * 
   * @param e その他の例外
   * @param req リクエスト
   * @return ステータースコード500のレスポンス
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest req) {
    e.printStackTrace();
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
}
