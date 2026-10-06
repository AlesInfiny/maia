package com.dressca.applicationmodules.shopping.valueobject;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * 日本の住所を表現する値オブジェクトです。
 */
public record Address(@NonNull String postalCode, @NonNull String todofuken,
    @NonNull String shikuchoson, @NonNull String azanaAndOthers) {

  /**
   * {@link Address} クラスのインスタンスを空文字で初期化します。
   */
  public Address() {
    this(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
  }
}
