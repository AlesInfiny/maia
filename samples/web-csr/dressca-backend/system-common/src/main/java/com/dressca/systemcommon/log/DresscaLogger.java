package com.dressca.systemcommon.log;

import java.util.Map;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーのインターフェースです。
 */
public interface DresscaLogger {

  /**
   * DEBUG レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void debug(String msg);

  /**
   * Key Value を追加して DEBUG レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void debug(String msg, Map<String, String> keyValueMap);

  /**
   * ERROR レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void error(String msg);

  /**
   * Key Value を追加して ERROR レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void error(String msg, Map<String, String> keyValueMap);

  /**
   * INFO レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void info(String msg);

  /**
   * Key Value を追加して INFO レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void info(String msg, Map<String, String> keyValueMap);

  /**
   * TRACE レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void trace(String msg);

  /**
   * Key Value を追加して TRACE レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void trace(String msg, Map<String, String> keyValueMap);

  /**
   * WARN レベルのログを出力します。
   * 
   * @param msg ログのメッセージ。
   */
  public void warn(String msg);

  /**
   * Key Value を追加して WARN レベルのログを出力します。
   * 
   * @param msg         ログのメッセージ。
   * @param keyValueMap Key Value のマップ。
   */
  public void warn(String msg, Map<String, String> keyValueMap);
}