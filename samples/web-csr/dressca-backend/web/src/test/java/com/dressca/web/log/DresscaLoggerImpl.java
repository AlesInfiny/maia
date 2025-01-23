package com.dressca.web.log;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーです。単体テストで使用します。
 */
@Component
@NoArgsConstructor
public class DresscaLoggerImpl implements DresscaLogger {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 構造化ログでデフォルトで出力する内容を追加します。
   * try ブロック内に MDC.put(key, value) で追記してください。
   * 
   * @param logAction ログを出力するメソッド。
   */
  private void logWithMdc(Runnable logAction) {
    try {
      // key-value の組を追加してください
      logAction.run();
    } finally {
      MDC.clear();
    }
  }

  @Override
  public void debug(String msg) {
    logWithMdc(() -> apLog.debug(msg));
  }

  @Override
  public void debug(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atDebug().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  @Override
  public void error(String msg) {
    logWithMdc(() -> apLog.error(msg));
  }

  @Override
  public void error(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atError().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  @Override
  public void info(String msg) {
    logWithMdc(() -> apLog.info(msg));
  }

  @Override
  public void info(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atInfo().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  @Override
  public void trace(String msg) {
    logWithMdc(() -> apLog.trace(msg));
  }

  @Override
  public void trace(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atTrace().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  @Override
  public void warn(String msg) {
    logWithMdc(() -> apLog.warn(msg));
  }

  @Override
  public void warn(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atWarn().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }
}
