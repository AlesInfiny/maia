package com.dressca.web.log;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.NoArgsConstructor;

/**
 * アプリケーション固有のログ出力を実装した構造化ロガーです。
 */
@Component
@NoArgsConstructor
public class DresscaStructuredLoggerImpl extends AbstractStructuredLogger {

  @Override
  protected void logWithMdc(Runnable logAction) {
    try {
      // サンプルアプリでは sessionId を発行していないため、 null としています。
      MDC.put("sessionId", null);
      logAction.run();
    } finally {
      MDC.remove("sessionId");
    }
  }
}
