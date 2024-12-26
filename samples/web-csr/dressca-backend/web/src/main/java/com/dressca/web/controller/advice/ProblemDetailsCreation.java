package com.dressca.web.controller.advice;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import com.dressca.web.constant.ProblemDetailsConstant;
import com.dressca.web.log.ErrorMessageBuilder;

/**
 * エラーレスポンスに含める ProblemDetails を作成するクラスです。
 */
@Component
public class ProblemDetailsCreation {

  @Autowired
  private Environment env;

  /**
   * その他のシステムエラーをステータースコード500で返却する。
   *
   * @param errorBuilder 例外ビルダー
   * @param title        タイトル
   * @param status       ステータスコード
   * @return エラーレスポンスに格納する ProblemDetails
   */
  public ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String title, HttpStatus status) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(status);

    problemDetail.setTitle(title);

    String[] activeProfiles = env.getActiveProfiles();
    if (activeProfiles.length == 0) {
      activeProfiles = env.getDefaultProfiles();
    }

    // local 環境においては detail を含める
    if (Arrays.stream(activeProfiles).filter(profile -> Objects.equals(profile, "local"))
        .findFirst().isPresent()) {
      problemDetail.setDetail(errorBuilder.createLogMessageStackTrace());
    }

    Map<String, Object> errorProperty = new LinkedHashMap<String, Object>() {
      {
        put(ProblemDetailsConstant.EXCEPTION_ID, errorBuilder.getExceptionId());
        put(ProblemDetailsConstant.EXCEPTION_VALUES, errorBuilder.getFrontMessageValue());
      }
    };

    problemDetail.setProperties(errorProperty);

    return problemDetail;
  }
}
