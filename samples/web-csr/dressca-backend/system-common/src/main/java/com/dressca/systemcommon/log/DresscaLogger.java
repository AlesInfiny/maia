package com.dressca.systemcommon.log;

import java.util.Map;

/**
 * コンテキストにログを保持させる機能を追加したカスタムロガーのインターフェースです。
 */
public interface DresscaLogger {

  public void debug(String msg);

  public void debug(String msg, Map<String, String> keyValueMap);

  public void error(String msg);

  public void error(String msg, Map<String, String> keyValueMap);

  public void info(String msg);

  public void info(String msg, Map<String, String> keyValueMap);

  public void trace(String msg);

  public void trace(String msg, Map<String, String> keyValueMap);

  public void warn(String msg);

  public void warn(String msg, Map<String, String> keyValueMap);
}