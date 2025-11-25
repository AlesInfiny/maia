package com.dressca.cms.systemcommon.constant;

import java.util.Locale;
import java.util.Map;

/**
 * 言語コードの定数クラスです。
 */
public class LanguageCodeConstants {

  /** 日本語のロケールです。 */
  public static final Locale LOCALE_JA = Locale.JAPANESE;
  /** 英語のロケールです。 */
  public static final Locale LOCALE_EN = Locale.ENGLISH;
  /** 中国語のロケールです。 */
  public static final Locale LOCALE_ZH = Locale.CHINESE;
  /** スペイン語のロケールです。 */
  public static final Locale LOCALE_ES = Locale.of("es");

  /** 日本語の言語コードです。 */
  public static final String JA = LOCALE_JA.getLanguage();
  /** 英語の言語コードです。 */
  public static final String EN = LOCALE_EN.getLanguage();
  /** 中国語の言語コードです。 */
  public static final String ZH = LOCALE_ZH.getLanguage();
  /** スペイン語の言語コードです。 */
  public static final String ES = LOCALE_ES.getLanguage();

  /** 言語コードの優先順位マップです。 */
  public static final Map<String, Integer> LANGUAGE_CODE_PRIORITY = Map.of(
      JA, 1,
      EN, 2,
      ZH, 3,
      ES, 4);
}
