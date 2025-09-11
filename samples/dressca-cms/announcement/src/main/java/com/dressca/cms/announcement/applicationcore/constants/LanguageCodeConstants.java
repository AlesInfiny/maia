package com.dressca.cms.announcement.applicationcore.constants;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 言語コード用の定数クラスです。
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

  /** 言語コードと言語名のマップです。 */
  public static final Map<String, String> LANGUAGE_CODE_MAP;

  static {
    Map<String, String> map = new LinkedHashMap<>();
    map.put(JA, "日本語");
    map.put(EN, "英語");
    map.put(ZH, "中国語");
    map.put(ES, "スペイン語");
    LANGUAGE_CODE_MAP = Collections.unmodifiableMap(map);
  }

  /** 言語コードのリストです。 */
  public static final List<String> SUPPORTED_LANGUAGE_CODES = List.of(JA, EN, ZH, ES);

}
