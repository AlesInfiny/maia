package com.dressca.systemcommon.log;

import java.util.Map;

/**
 * コンテキストにログ出力を保持させるためのラッパークラスのインターフェースです。
 */
public interface LoggerWrapper {

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