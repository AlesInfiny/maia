package com.dressca.cms.web.constants;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DisplayPriorityConstants {
  public static final int EMERGENCY = 1;
  public static final int HIGH = 2;
  public static final int MEDIUM = 3;
  public static final int LOW = 4;
  public static final Map<Integer, String> DISPLAY_PRIORITY_MAP;
  static {
    Map<Integer, String> map = new LinkedHashMap<>();
    map.put(EMERGENCY, "緊急");
    map.put(HIGH, "大");
    map.put(MEDIUM, "中");
    map.put(LOW, "小");
    DISPLAY_PRIORITY_MAP = Collections.unmodifiableMap(map);
  }
}
