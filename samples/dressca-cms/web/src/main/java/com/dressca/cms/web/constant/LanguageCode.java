package com.dressca.cms.web.constant;

import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;

/**
 * 言語コードの選択肢です。
 */
public enum LanguageCode {

  /** 日本語の言語コードです。 */
  JAPANESE(LanguageCodeConstants.LOCALE_JA.getLanguage(), "日本語"),
  /** 英語の言語コードです。 */
  ENGLISH(LanguageCodeConstants.LOCALE_EN.getLanguage(), "英語"),
  /** 中国語の言語コードです。 */
  CHINESE(LanguageCodeConstants.LOCALE_ZH.getLanguage(), "中国語"),
  /** スペイン語の言語コードです。 */
  SPANISH(LanguageCodeConstants.LOCALE_ES.getLanguage(), "スペイン語");

  LanguageCode(String value, String label) {
    this.value = value;
    this.label = label;
  }

  private final String value;
  private final String label;

  public String getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }
}
