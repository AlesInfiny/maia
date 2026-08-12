package com.dressca.web.admin.log;

import com.dressca.domainmodules.authorization.UserStore;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * アプリケーション固有のログ出力を実装した構造化ロガーです。
 */
@Component
@RequiredArgsConstructor
public class DresscaStructuredLoggerImpl extends AbstractStructuredLogger {

  private final UserStore userStore;

  @Override
  protected void logWithMdc(Runnable logAction) {
    String userName = userStore.getLoginUserName();
    try {
      MDC.put("userId", userName);
      // サンプルアプリでは sessionId を発行していないため、 null としています。
      MDC.put("sessionId", null);
      logAction.run();
    } finally {
      MDC.remove("userId");
      MDC.remove("sessionId");
    }
  }
}
