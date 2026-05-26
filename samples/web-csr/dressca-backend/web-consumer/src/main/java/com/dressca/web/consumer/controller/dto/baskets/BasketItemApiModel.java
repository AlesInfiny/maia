package com.dressca.web.consumer.controller.dto.baskets;

import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごに格納された単一の商品情報を格納する dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemApiModel {
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID catalogItemId;
  @NotNull
  private BigDecimal unitPrice;
  @NotNull
  private int quantity;
  @NotNull
  private BigDecimal subTotal;
  private CatalogItemSummaryApiModel catalogItem;
}
