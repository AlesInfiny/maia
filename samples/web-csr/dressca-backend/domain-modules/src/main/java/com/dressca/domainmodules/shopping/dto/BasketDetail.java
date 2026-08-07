package com.dressca.domainmodules.shopping.dto;

import java.util.List;
import java.util.UUID;
import com.dressca.domainmodules.shopping.model.Basket;
import com.dressca.domainmodules.shopping.model.DisplayItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 買い物かごの詳細を格納するクラスです。
 */
@AllArgsConstructor
@Getter
public class BasketDetail {
  private Basket basket;
  private List<DisplayItem> displayItems;
  private List<UUID> deletedItemIds;
}
