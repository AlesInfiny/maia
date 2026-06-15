package com.dressca.applicationcore.order;

import lombok.NonNull;
import lombok.Value;

/**
 * 注文された掲載品を管理する値オブジェクトです。
 * 
 * <ul>
 * <li>この値オブジェクトは、注文時点での掲載品エンティティのスナップショットです。</li>
 * <li>注文確定後に掲載品情報が変更されたとしても、注文情報は変更されるべきではないためです。</li>
 * </ul>
 */
@Value
public class DisplayItemOrdered {
  private long displayItemId;
  @NonNull
  private String productName;
  @NonNull
  private String productCode;
}
