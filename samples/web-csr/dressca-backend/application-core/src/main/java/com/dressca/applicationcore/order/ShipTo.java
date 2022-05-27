package com.dressca.applicationcore.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * 商品のお届け先を表現する値オブジェクトです.
 */
// @Value
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipTo {
  // @NonNull
  private String fullName;
  // @NonNull
  private Address address;
}
