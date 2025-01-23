package com.dressca.web.log;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーです。単体テストで使用します。
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
