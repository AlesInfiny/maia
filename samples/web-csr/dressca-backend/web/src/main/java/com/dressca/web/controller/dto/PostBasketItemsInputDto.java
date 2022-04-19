package com.dressca.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostBasketItemsInputDto {
  private long catalogItemId;
  private int addedQuantity = 1;
}
