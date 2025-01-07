package com.dressca.web.consumer.controller.dto.baskets;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごにアイテムを追加する際に用いるdtoクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBasketItemsRequest {
  @NotNull
  private long catalogItemId;
  private int addedQuantity = 1;
}
