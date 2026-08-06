package com.dressca.web.consumer.controller.dto.order;

import com.dressca.web.consumer.controller.dto.displayitem.DisplayItemSummaryApiModel;
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
  private UUID id;
  private DisplayItemSummaryApiModel itemOrdered;
  @NotNull
  private int quantity;
  @NotNull
  private BigDecimal unitPrice;
  @NotNull
  private BigDecimal subTotal;
}
