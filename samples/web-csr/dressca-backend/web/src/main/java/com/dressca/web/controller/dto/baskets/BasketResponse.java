package com.dressca.web.controller.dto.baskets;

import java.util.List;
import com.dressca.web.controller.dto.accounting.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponse {
  private String buyerId;
  private AccountResponse account;
  private List<BasketItemResponse> basketItems;
}
