package com.dressca.web.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
  public long id;
  public CatalogItemSummaryDto itemOrdered;
  public int quantity;
  public BigDecimal unitPrice;
  public BigDecimal subTotal;
}
