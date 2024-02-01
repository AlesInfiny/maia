package com.dressca.web.controller.dto.order;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import com.dressca.web.controller.dto.catalog.CatalogItemSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文アイテムを取得する際に用いるdtoクラスです。
 */
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
