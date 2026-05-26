package com.dressca.web.consumer.controller.dto.baskets;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごにアイテムを追加する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBasketItemsRequest {
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID catalogItemId;
  private int addedQuantity = 1;
}
