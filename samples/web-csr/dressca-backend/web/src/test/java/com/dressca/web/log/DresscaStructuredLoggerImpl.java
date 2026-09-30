package com.dressca.web.log;

import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.NoArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

/**
 * アプリケーション固有のログ出力を実装した構造化ロガーです。単体テストで使用します。
 * テスト専用のコンポーネントのため、 {@link TestComponent} を付与してコンポーネントスキャンの対象外としています。
 * 利用する場合はテストクラスで明示的にインポートしてください。
 */
@TestComponent
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
