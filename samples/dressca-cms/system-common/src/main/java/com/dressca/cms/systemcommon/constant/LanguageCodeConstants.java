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

  /** 言語コードの優先順位マップです。 */
  public static final Map<String, Integer> LANGUAGE_CODE_PRIORITY = Map.of(
      LOCALE_JA.getLanguage(), 1,
      LOCALE_EN.getLanguage(), 2,
      LOCALE_ZH.getLanguage(), 3,
      LOCALE_ES.getLanguage(), 4);
}
