package com.dressca.cms.announcement.applicationcore.constant;

import java.util.Map;

/**
 * 言語コードの定数クラスです。
 */
public class LanguageCodeConstants {

  /** 日本語の言語コードです。 */
  public static final String JA = "ja";
  /** 英語の言語コードです。 */
  public static final String EN = "en";
  /** 中国語の言語コードです。 */
  public static final String ZH = "zh";
  /** スペイン語の言語コードです。 */
  public static final String ES = "es";

  /** 言語コードの優先順位マップです。 */
  public static final Map<String, Integer> LANGUAGE_CODE_PRIORITY = Map.of(
      JA, 1,
      EN, 2,
      ZH, 3,
      ES, 4);
}
