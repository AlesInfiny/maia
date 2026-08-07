package com.dressca.domainmodules.shopping.model;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * 日本の住所を表現する値オブジェクトです。
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

  /**
   * {@link Address} クラスのインスタンスを初期化します。
   */
  public Address() {
    postalCode = StringUtils.EMPTY;
    todofuken = StringUtils.EMPTY;
    shikuchoson = StringUtils.EMPTY;
    azanaAndOthers = StringUtils.EMPTY;
  }
}
