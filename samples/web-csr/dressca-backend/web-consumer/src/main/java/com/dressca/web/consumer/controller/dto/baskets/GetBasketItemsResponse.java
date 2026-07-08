package com.dressca.web.consumer.controller.dto.baskets;

import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
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
  private UUID buyerId;
  private AccountApiModel account;
  private List<BasketItemApiModel> basketItems;
  private List<UUID> deletedItemIds;
}
