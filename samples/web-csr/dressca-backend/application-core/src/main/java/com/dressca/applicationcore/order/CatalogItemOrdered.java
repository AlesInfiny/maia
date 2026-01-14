package com.dressca.applicationcore.order;

import lombok.NonNull;
import lombok.Value;

/**
 * 注文されたカタログアイテムを管理する値オブジェクトです。
 * 
 * <ul>
 * <li>この値オブジェクトは、注文時点でのカタログアイテムエンティティのスナップショットです。</li>
 * <li>注文確定後にカタログ情報が変更されたとしても、注文情報は変更されるべきではないためです。</li>
 * </ul>
 */
@Value
public class CatalogItemOrdered {
  private long catalogItemId;
  @NonNull
  private String productName;
  @NonNull
  private String productCode;
}
