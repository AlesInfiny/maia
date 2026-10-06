package com.dressca.applicationmodules.shopping.valueobject;

/**
 * 商品のお届け先を表現する値オブジェクトです。
 */
public record ShipTo(String fullName, Address address) {
}
