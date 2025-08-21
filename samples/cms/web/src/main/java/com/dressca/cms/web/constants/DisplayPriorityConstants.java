package com.dressca.cms.web.constants;

import java.util.Map;

public final class DisplayPriorityConstants {
  public static final int EMERGENCY = 1;
  public static final int HIGH = 2;
  public static final int MEDIUM = 3;
  public static final int LOW = 4;

  public static final Map<Integer, String> DISPLAY_PRIORITY_MAP =
      Map.of(EMERGENCY, "緊急", HIGH, "大", MEDIUM, "中", LOW, "小");
}
