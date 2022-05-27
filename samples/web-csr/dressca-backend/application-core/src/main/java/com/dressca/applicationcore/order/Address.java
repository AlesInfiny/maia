package com.dressca.applicationcore.order;

import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * 日本の住所を表現する値オブジェクトです.
 */
@Value
@AllArgsConstructor
public class Address {
  @NonNull
  private String postalCode;
  @NonNull
  private String todofuken;
  @NonNull
  private String shikuchoson;
  @NonNull
  private String azanaAndOthers;

  public Address() {
    postalCode = StringUtils.EMPTY;
    todofuken = StringUtils.EMPTY;
    shikuchoson = StringUtils.EMPTY;
    azanaAndOthers = StringUtils.EMPTY;
  }
}
