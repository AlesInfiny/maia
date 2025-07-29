package com.dressca.systemcommon.log;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dressca.systemcommon.constant.SystemPropertyConstants;

/**
 * コンテキストにログを保持させる機能を追加した構造化ロガーのインターフェースです。
 */
public abstract class AbstractStructuredLogger {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 構造化ログでデフォルトで出力する内容を追加します。
   * try ブロック内に MDC.put(key, value) で追記してください。
   * 
   * @param logAction ログを出力するメソッド。
   */
  protected abstract void logWithMdc(Runnable logAction);

  /**
   * DEBUG レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
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