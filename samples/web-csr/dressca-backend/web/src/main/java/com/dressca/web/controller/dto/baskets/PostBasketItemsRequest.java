package com.dressca.web.controller.dto.baskets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBasketItemsRequest {
  private long catalogItemId;
  private int addedQuantity = 1;
}
