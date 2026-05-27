package com.dressca.web.consumer.controller.dto.baskets;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテムの一覧を格納するための dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBasketItemsResponse {
  @NotNull
  private String buyerId;
  private AccountApiModel account;
  private List<BasketItemApiModel> basketItems;
  private List<Long> deletedItemIds;
}
