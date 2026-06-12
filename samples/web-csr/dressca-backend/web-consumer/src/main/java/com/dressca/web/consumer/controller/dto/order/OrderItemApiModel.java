package com.dressca.web.consumer.controller.dto.order;

import com.dressca.web.consumer.controller.dto.catalog.CatalogItemSummaryApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文アイテムを取得する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemApiModel {
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID id;
  private CatalogItemSummaryApiModel itemOrdered;
  @NotNull
  private int quantity;
  @NotNull
  private BigDecimal unitPrice;
  @NotNull
  private BigDecimal subTotal;
}
