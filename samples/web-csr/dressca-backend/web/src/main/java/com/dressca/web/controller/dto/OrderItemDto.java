package com.dressca.web.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
  public long Id;
  public CatalogItemSummaryDto itemOrdered;
  public int quantity;
  public BigDecimal subTotal;
}
