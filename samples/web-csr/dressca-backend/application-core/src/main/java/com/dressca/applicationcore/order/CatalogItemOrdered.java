package com.dressca.applicationcore.order;

import lombok.NonNull;
import lombok.Value;

/**
 * 注文されたカタログアイテムを管理する値オブジェクトです。
 * 
 * <p>
 * この値オブジェクトは、注文時点でのカタログアイテムエンティティのスナップショットです。
 * これは、注文確定後にカタログ情報が変更されたとしても、注文情報は変更されるべきではないためです。
 * </p>
 */
@Value
public class CatalogItemOrdered {
  private long catalogItemId;
  @NonNull
  private String productName;
  @NonNull
  private String productCode;
}
