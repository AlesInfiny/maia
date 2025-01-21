package com.dressca.web.consumer.log;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.log.DresscaLogger;
import lombok.NoArgsConstructor;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーです。
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
      // サンプルアプリでは sessionId を発行していないため、 null としています。
      MDC.put("sessionId", null);
      logAction.run();
    } finally {
      MDC.clear();
    }
  }

  public void debug(String msg) {
    logWithMdc(() -> apLog.debug(msg));
  }

  /**
   * Key Value を追加して DEBUG レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void debug(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atDebug().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  /**
   * ERROR レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void error(String msg) {
    logWithMdc(() -> apLog.error(msg));
  }

  /**
   * Key Value を追加して ERROR レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void error(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atError().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  /**
   * INFO レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void info(String msg) {
    logWithMdc(() -> apLog.info(msg));
  }

  /**
   * Key Value を追加して INFO レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void info(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atInfo().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  /**
   * TRACE レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void trace(String msg) {
    logWithMdc(() -> apLog.trace(msg));
  }

  /**
   * Key Value を追加して TRACE レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void trace(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atTrace().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }

  /**
   * WARN レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void warn(String msg) {
    logWithMdc(() -> apLog.warn(msg));
  }

  /**
   * Key Value を追加して WARN レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void warn(String msg, Map<String, String> keyValueMap) {
    logWithMdc(() -> {
      var logBuilder = apLog.atWarn().setMessage(msg);
      keyValueMap.forEach(logBuilder::addKeyValue);
      logBuilder.log();
    });
  }
}
