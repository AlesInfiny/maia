package com.dressca.cms.web.constant;

import java.util.Map;

/**
 * 表示優先度の定数クラスです。
 */
public class DisplayPriorityConstants {

  /** 緊急の表示優先度です。 */
  public static final int EMERGENCY = 1;
  /** 高の表示優先度です。 */
  public static final int HIGH = 2;
  /** 中の表示優先度です。 */
  public static final int MEDIUM = 3;
  /** 低の表示優先度です。 */
  public static final int LOW = 4;

  /** 表示優先度のラベルマッピングです。 */
  public static final Map<Integer, String> DISPLAY_PRIORITY_LABELS = Map.of(
      EMERGENCY, "緊急",
      HIGH, "高",
      MEDIUM, "中",
      LOW, "低");
}
