package com.example.auth.controller.common;

import com.example.auth.common.exception.ExceptionIdConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Locale;

/**
 * エラーハンドラー
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * すべての例外をキャッチする
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = createApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return this.handleExceptionInternal(ex, apiErrorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * エラーレスポンスを作成する (500エラー)
     *
     * @param ex
     * @return
     */
    private ApiErrorResponse createApiError(Exception ex, HttpStatusCode status) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        String errMessage = messageSource.getMessage(ExceptionIdConstant.E_COMMON_001, null, Locale.getDefault());

        apiErrorResponse.setStatus(status.value());
        apiErrorResponse.setTitle(errMessage);
        apiErrorResponse.setDetails(null);
        apiErrorResponse.setResponseDt(OffsetDateTime.now());

        return apiErrorResponse;
    }
}
