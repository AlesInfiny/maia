package com.dressca.applicationmodules.shopping.valueobject;

import java.util.UUID;
import lombok.NonNull;
import lombok.Value;

/**
 * 注文された陳列品を管理する値オブジェクトです。
 *
 * <ul>
 * <li>この値オブジェクトは、注文時点での陳列品エンティティのスナップショットです。</li>
 * <li>注文確定後に陳列品情報が変更されたとしても、注文情報は変更されるべきではないためです。</li>
 * </ul>
 */
@Value
public class DisplayItemOrdered {
  private UUID displayItemId;
  @NonNull
  private String productName;
  @NonNull
  private String productCode;
}
