package com.dressca.web.controller.dto.order;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import com.dressca.web.controller.dto.catalog.CatalogItemSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
  @NotNull
  public long id;
  public CatalogItemSummaryResponse itemOrdered;
  @NotNull
  public int quantity;
  @NotNull
  public BigDecimal unitPrice;
  @NotNull
  public BigDecimal subTotal;
}
