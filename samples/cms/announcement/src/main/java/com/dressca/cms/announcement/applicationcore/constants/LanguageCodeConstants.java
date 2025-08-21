package com.dressca.cms.announcement.applicationcore.constants;

import java.util.List;
import java.util.Map;

/**
 * 言語コード用の定数クラスです。
 */
public class LanguageCodeConstants {

  private static final String JA = "ja";
  private static final String EN = "en";
  private static final String ZH = "zh";
  private static final String ES = "es";

  public static final Map<String, String> LANGUAGE_CODE_MAP =
      Map.of(JA, "日本語", EN, "英語", ZH, "中国語", ES, "スペイン語");

  public static final List<String> SUPPORTED_LANGUAGE_CODES = List.of(JA, EN, ZH, ES);

}
