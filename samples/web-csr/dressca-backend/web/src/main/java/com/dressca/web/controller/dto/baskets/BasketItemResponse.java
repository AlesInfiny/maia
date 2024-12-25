package com.dressca.web.controller.dto.baskets;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import com.dressca.web.controller.dto.catalog.CatalogItemSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごに格納された単一の商品情報を格納するレスポンスクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemResponse {
  @NotNull
  private long catalogItemId;
  @NotNull
  private BigDecimal unitPrice;
  @NotNull
  private int quantity;
  @NotNull
  private BigDecimal subTotal;
  private CatalogItemSummaryResponse catalogItem;
}