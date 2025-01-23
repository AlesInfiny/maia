package com.dressca.web.consumer.log;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーです。
 */
@Component
@NoArgsConstructor
public class DresscaLoggerImpl extends DresscaLogger {

  @Override
  protected void logWithMdc(Runnable logAction) {
    try {
      // サンプルアプリでは sessionId を発行していないため、 null としています。
      MDC.put("sessionId", null);
      logAction.run();
    } finally {
      MDC.clear();
    }
  }
}
