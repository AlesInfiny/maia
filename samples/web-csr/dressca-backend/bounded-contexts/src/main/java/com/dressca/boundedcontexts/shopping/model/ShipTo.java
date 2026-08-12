package com.dressca.boundedcontexts.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品のお届け先を表現する値オブジェクトです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipTo {
  private String fullName;
  private Address address;
}
