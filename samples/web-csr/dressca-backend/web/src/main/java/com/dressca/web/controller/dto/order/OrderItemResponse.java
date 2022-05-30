package com.dressca.web.controller.dto.order;

import java.math.BigDecimal;
import com.dressca.web.controller.dto.catalog.CatalogItemSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
  public long id;
  public CatalogItemSummaryResponse itemOrdered;
  public int quantity;
  public BigDecimal unitPrice;
  public BigDecimal subTotal;
}
