package com.dressca.web.controller.advice;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import com.dressca.web.constant.WebConstants;
import com.dressca.web.log.ErrorMessageBuilder;

/**
 * エラーレスポンスに含める ProblemDetails を作成するクラスです。
 */
@Component
public class ProblemDetailsFactory {

  @Autowired
  private Environment env;

  @Autowired
  private MessageSource messages;

  /**
   * エラーレスポンスに含める ProblemDetails を作成する。
   *
   * @param errorBuilder 例外ビルダー。
   * @param titleId      タイトルのメッセージ ID 。
   * @param status       ステータスコード。
   * @return エラーレスポンスに格納する ProblemDetails 。
   */
  public ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String titleId, HttpStatus status) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(status);

    problemDetail.setTitle(messages.getMessage(titleId, new String[] {}, Locale.getDefault()));

    // 開発環境においては、 detail プロパティにスタックトレースを含める
    // 開発環境かどうかの判断は、環境変数の Profile をもとに判断する
    String[] activeProfiles = env.getActiveProfiles();
    if (activeProfiles.length == 0) {
      activeProfiles = env.getDefaultProfiles();
    }

    if (Arrays.stream(activeProfiles).filter(profile -> Objects.equals(profile, "local"))
        .findFirst().isPresent()) {
      problemDetail.setDetail(errorBuilder.createLogMessageStackTrace());
    }

    // 拡張メンバーとして exceptionId と exceptionValues を含める
    Map<String, Object> errorProperty = new LinkedHashMap<String, Object>() {
      {
        put(WebConstants.EXCEPTION_ID, errorBuilder.getExceptionId());
        put(WebConstants.EXCEPTION_VALUES, errorBuilder.getFrontMessageValue());
      }
    };

    problemDetail.setProperties(errorProperty);

    return problemDetail;
  }
}
