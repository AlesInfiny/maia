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
  private long id;
  private CatalogItemSummaryResponse itemOrdered;
  @NotNull
  private int quantity;
  @NotNull
  private BigDecimal unitPrice;
  @NotNull
  private BigDecimal subTotal;
}
