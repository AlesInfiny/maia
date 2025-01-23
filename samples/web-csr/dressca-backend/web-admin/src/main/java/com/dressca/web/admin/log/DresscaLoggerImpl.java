package com.dressca.web.admin.log;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーです。
 */
@Component
@NoArgsConstructor
public class DresscaLoggerImpl extends DresscaLogger {

  @Autowired
  private UserStore userStore;

  @Override
  protected void logWithMdc(Runnable logAction) {
    String userName = userStore.getLoginUserName();
    try {
      MDC.put("userId", userName);
      // サンプルアプリでは sessionId を発行していないため、 null としています。
      MDC.put("sessionId", null);
      logAction.run();
    } finally {
      MDC.clear();
    }
  }
}
