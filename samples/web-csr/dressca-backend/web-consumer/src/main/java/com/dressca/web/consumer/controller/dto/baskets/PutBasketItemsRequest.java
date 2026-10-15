package com.dressca.web.consumer.controller.dto.baskets;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテム内の数量を変更する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutBasketItemsRequest {
  @NotNull
  private UUID displayItemId;
  @NotNull
  private int quantity;
}
