package com.dressca.web.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasketDto {
   private String buyerId;
   private AccountDto account;
   private List<BasketItemDto> basketItems;
}
