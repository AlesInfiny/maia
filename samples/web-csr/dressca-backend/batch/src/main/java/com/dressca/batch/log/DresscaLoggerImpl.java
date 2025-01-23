package com.dressca.batch.log;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログ出力を保持させるためのラッパークラスです。
 */
@Component
@NoArgsConstructor
public class DresscaLoggerImpl extends DresscaLogger {

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
