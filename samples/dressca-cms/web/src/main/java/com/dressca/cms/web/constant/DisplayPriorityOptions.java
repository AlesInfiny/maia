package com.dressca.cms.web.constant;

import com.dressca.cms.announcement.applicationcore.constant.DisplayPriorityConstants;

/**
 * 表示優先度の選択肢です。
 */
public enum DisplayPriorityOptions {

  /** 緊急の表示優先度です。 */
  EMERGENCY(DisplayPriorityConstants.EMERGENCY, "緊急"),
  /** 高の表示優先度です。 */
  HIGH(DisplayPriorityConstants.HIGH, "高"),
  /** 中の表示優先度です。 */
  MEDIUM(DisplayPriorityConstants.MEDIUM, "中"),
  /** 低の表示優先度です。 */
  LOW(DisplayPriorityConstants.LOW, "低");

  private final int value;
  private final String label;

  /**
   * 値とラベルを指定して {@link DisplayPriorityOptions} を初期化します。
   *
   * @param value 表示優先度の値。
   * @param label 表示優先度のラベル。
   */
  DisplayPriorityOptions(int value, String label) {
    this.value = value;
    this.label = label;
  }

  /**
   * 表示優先度の値を取得します。
   *
   * @return 表示優先度の値。
   */
  public int getValue() {
    return value;
  }

  /**
   * 表示優先度のラベルを取得します。
   *
   * @return 表示優先度のラベル。
   */
  public String getLabel() {
    return label;
  }
}
