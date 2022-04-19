package com.dressca.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PutBasketItemInputDto {
  private long catalogItemId;
  private int quentity;
}
