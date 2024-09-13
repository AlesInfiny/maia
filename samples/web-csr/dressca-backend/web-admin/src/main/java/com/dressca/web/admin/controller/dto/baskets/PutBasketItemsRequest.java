package com.dressca.web.admin.controller.dto.baskets;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテム内の数量を変更する際に用いるdtoクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutBasketItemsRequest {
  @NotNull
  private long catalogItemId;
  @NotNull
  private int quantity;
}
