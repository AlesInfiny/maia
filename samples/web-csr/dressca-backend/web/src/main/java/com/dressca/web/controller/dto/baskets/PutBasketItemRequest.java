package com.dressca.web.controller.dto.baskets;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutBasketItemRequest {
  @NotNull
  private long catalogItemId;
  @NotNull
  private int quantity;
}
