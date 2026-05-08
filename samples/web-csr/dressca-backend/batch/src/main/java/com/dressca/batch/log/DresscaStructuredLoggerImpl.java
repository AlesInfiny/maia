package com.dressca.batch.log;

import org.springframework.stereotype.Component;
import com.dressca.modules.log.AbstractStructuredLogger;
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
      // key-value の組を追加してください
      logAction.run();
    } finally {
      // 以下のように追加した key を指定して remove してください
      // MDC.remove(key);
    }
  }
}
