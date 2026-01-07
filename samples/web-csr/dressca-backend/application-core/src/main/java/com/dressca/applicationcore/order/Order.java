package com.dressca.applicationcore.order;

import com.dressca.applicationcore.accounting.Account;
import com.dressca.applicationcore.accounting.AccountItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文のエンティティです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  private long id;
  private List<OrderItem> orderItems = new ArrayList<>();
  private Account account;
  private String buyerId;
  private LocalDateTime orderDate = LocalDateTime.now();
  private ShipTo shipToAddress;
  private double consumptionTaxRate;
  private BigDecimal totalItemsPrice;
  private BigDecimal deliveryCharge;
  private BigDecimal consumptionTax;
  private BigDecimal totalPrice;

  /**
   * {@link Order} クラスのインスタンスを初期化します。
   * 
   * @param buyerId 購入者 ID 。
   * @param shipToAddress 宛先住所。
   * @param orderItems 商品リスト。
   */
  public Order(String buyerId, ShipTo shipToAddress, List<OrderItem> orderItems) {
    this.buyerId = buyerId;
    this.shipToAddress = shipToAddress;
    this.orderItems = orderItems;
    this.account = new Account(orderItems.stream()
        .map(item -> new AccountItem(item.getQuantity(), item.getUnitPrice()))
        .collect(Collectors.toList()));
    this.consumptionTaxRate = Account.CONSUMPTION_TAX_RATE;
    this.totalItemsPrice = this.account.getItemTotalPrice();
    this.deliveryCharge = this.account.getDeliveryCharge();
    this.consumptionTax = this.account.getConsumptionTax();
    this.totalPrice = this.account.getTotalPrice();
  }

  /**
   * {@link Order} クラスのインスタンスを初期化します。
   * 
   * @param id ID 。
   * @param buyerId 購入者 ID 。
   * @param orderDate 注文日付。
   * @param shipToAddress 宛先住所。
   * @param consumptionTaxRate 消費税率。
   * @param totalItemsPrice 商品価格合計。
   * @param deliveryCharge 送料。
   * @param consumptionTax 消費税額。
   * @param totalPrice 合計料金。
   * @param orderItems 商品リスト。
   */
  public Order(long id, String buyerId, LocalDateTime orderDate, ShipTo shipToAddress,
      BigDecimal consumptionTaxRate, BigDecimal totalItemsPrice, BigDecimal deliveryCharge,
      BigDecimal consumptionTax, BigDecimal totalPrice, List<OrderItem> orderItems) {
    this.id = id;
    this.buyerId = buyerId;
    this.shipToAddress = shipToAddress;
    this.orderItems = orderItems;
    this.account = new Account(orderItems.stream()
        .map(item -> new AccountItem(item.getQuantity(), item.getUnitPrice()))
        .collect(Collectors.toList()));
    this.consumptionTaxRate = Account.CONSUMPTION_TAX_RATE;
    this.totalItemsPrice = this.account.getItemTotalPrice();
    this.deliveryCharge = this.account.getDeliveryCharge();
    this.consumptionTax = this.account.getConsumptionTax();
    this.totalPrice = this.account.getTotalPrice();
  }
}
