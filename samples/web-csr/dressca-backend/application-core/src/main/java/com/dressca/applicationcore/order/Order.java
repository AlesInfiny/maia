package com.dressca.applicationcore.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.dressca.applicationcore.accounting.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 注文のドメインモデル.
 */
@Data
@NoArgsConstructor
public class Order {
  private long id;
  private List<OrderItem> orderItems = List.of();
  private Account account;
  @NonNull
  private String buyerId;
  private LocalDateTime orderDate = LocalDateTime.now();
  @NonNull
  private ShipTo shipToAddress;
  private BigDecimal consumptionTaxRate;
  private BigDecimal totalItemsPrice;
  private BigDecimal deliveryCharge;
  private BigDecimal consumptionTax;
  private BigDecimal totalPrice;

  public Order(String buyerId, ShipTo shipToAddress) {
    this.buyerId = buyerId;
    this.shipToAddress = shipToAddress;
  }
}
