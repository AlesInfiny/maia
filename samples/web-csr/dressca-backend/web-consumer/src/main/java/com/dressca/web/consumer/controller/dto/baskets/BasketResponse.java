package com.dressca.web.consumer.controller.dto.baskets;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import com.dressca.web.consumer.controller.dto.accounting.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテムの一覧を格納するための dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponse {
  @NotNull
  private String buyerId;
  private AccountResponse account;
  private List<BasketItemResponse> basketItems;
}
