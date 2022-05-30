package com.dressca.web.controller.dto.baskets;

import java.math.BigDecimal;
import com.dressca.web.controller.dto.catalog.CatalogItemSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemResponse {
  private long catalogItemId;
  private BigDecimal unitPrice;
  private int quantity;
  private BigDecimal subTotal;
  private CatalogItemSummaryResponse catalogItem;
}
