package com.dressca.web.controller.dto.baskets;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBasketItemsRequest {
  @NotNull
  private long catalogItemId;
  private int addedQuantity = 1;
}
