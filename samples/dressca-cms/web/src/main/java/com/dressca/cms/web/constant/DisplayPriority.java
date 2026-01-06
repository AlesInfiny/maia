package com.dressca.cms.web.constant;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * 表示優先度の選択肢です。
 */
@Getter
public enum DisplayPriority {

  /** 緊急の表示優先度です。 */
  EMERGENCY(1, "緊急"),
  /** 高の表示優先度です。 */
  HIGH(2, "高"),
  /** 中の表示優先度です。 */
  MEDIUM(3, "中"),
  /** 低の表示優先度です。 */
  LOW(4, "低");

  private final int value;
  private final String label;

  /**
   * 表示優先度の値とラベルのマップです。
   */
  public static final Map<Integer, String> DISPLAY_PRIORITY_LABEL_MAP = Collections
      .unmodifiableMap(Stream.of(DisplayPriority.values())
          .collect(Collectors.toMap(DisplayPriority::getValue, DisplayPriority::getLabel)));

  /**
   * 値とラベルを指定して {@link DisplayPriority} を初期化します。
   *
   * @param value 表示優先度の値。
   * @param label 表示優先度のラベル。
   */
  DisplayPriority(int value, String label) {
    this.value = value;
    this.label = label;
  }
}
