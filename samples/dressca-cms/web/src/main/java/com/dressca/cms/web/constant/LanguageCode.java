package com.dressca.cms.web.constant;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;
import lombok.Getter;

/**
 * 言語コードの選択肢です。
 */
@Getter
public enum LanguageCode {

  /** 日本語の言語コードです。 */
  JAPANESE_OPTION(LanguageCodeConstants.LOCALE_JA.getLanguage(), "日本語"),
  /** 英語の言語コードです。 */
  ENGLISH_OPTION(LanguageCodeConstants.LOCALE_EN.getLanguage(), "英語"),
  /** 中国語の言語コードです。 */
  CHINESE_OPTION(LanguageCodeConstants.LOCALE_ZH.getLanguage(), "中国語"),
  /** スペイン語の言語コードです。 */
  SPANISH_OPTION(LanguageCodeConstants.LOCALE_ES.getLanguage(), "スペイン語");

  private final String value;
  private final String label;

  /**
   * 言語コードの値とラベルのマップです。
   */
  public static final Map<String, String> LANGUAGE_CODE_LABEL_MAP = Collections
      .unmodifiableMap(Stream.of(LanguageCode.values())
          .collect(Collectors.toMap(LanguageCode::getValue, LanguageCode::getLabel)));

  /**
   * 値とラベルを指定して {@link LanguageCode} を初期化します。
   *
   * @param value 言語コードの値。
   * @param label 言語コードのラベル。
   */
  LanguageCode(String value, String label) {
    this.value = value;
    this.label = label;
  }
}
