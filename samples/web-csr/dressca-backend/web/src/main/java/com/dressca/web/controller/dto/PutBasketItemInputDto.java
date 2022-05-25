package com.dressca.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutBasketItemInputDto {
  private long catalogItemId;
  private int quantity;
}
