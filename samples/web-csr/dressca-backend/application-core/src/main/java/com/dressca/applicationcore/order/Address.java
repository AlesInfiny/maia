package com.dressca.applicationcore.order;

import lombok.NonNull;
import lombok.Value;

/**
 * 日本の住所を表現する値オブジェクトです.
 */
@Value
public class Address {
  @NonNull
  private String postalCode;
  @NonNull
  private String todofuken;
  @NonNull
  private String shikuchoson;
  @NonNull
  private String azanaAndOthers;
}
