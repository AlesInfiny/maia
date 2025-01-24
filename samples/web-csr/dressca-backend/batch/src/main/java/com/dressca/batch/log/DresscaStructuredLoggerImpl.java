package com.dressca.batch.log;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加した構造化ロガーです。
 */
@Component
@NoArgsConstructor
public class DresscaStructuredLoggerImpl extends AbstractStructuredLogger {

  @Override
  protected void logWithMdc(Runnable logAction) {
    try {
      // key-value の組を追加してください
      logAction.run();
    } finally {
      MDC.clear();
    }
  }
}
