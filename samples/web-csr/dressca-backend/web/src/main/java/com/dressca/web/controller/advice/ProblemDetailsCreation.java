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

@Component
public class ProblemDetailsCreation {

  @Autowired
  private Environment env;

  public ProblemDetail createProblemDetail(ErrorMessageBuilder errorBuilder, String title, HttpStatus status) {
    Map<String, Object> errorProperty = new LinkedHashMap<String, Object>() {
      {
        put(ProblemDetailsConstant.EXCEPTION_ID, errorBuilder.getExceptionId());
        put(ProblemDetailsConstant.EXCEPTION_VALUES, errorBuilder.getFrontMessageValue());
      }
    };

    ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    // local 環境においては details を含める
    if (Arrays.stream(env.getActiveProfiles()).filter(profile -> Objects.equals(profile, "local"))
        .findFirst().isPresent()) {
      problemDetail.setDetail(errorBuilder.createLogMessageStackTrace());
    }
    problemDetail.setTitle(title);
    problemDetail.setProperties(errorProperty);
    return problemDetail;
  }
}
