package com.dressca.web.controller.dto.baskets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutBasketItemRequest {
  private long catalogItemId;
  private int quantity;
}
