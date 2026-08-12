package com.dressca.boundedcontexts.shopping.dto;

import com.dressca.boundedcontexts.shopping.model.Basket;
import com.dressca.boundedcontexts.shopping.model.DisplayItem;
import java.util.List;
import java.util.UUID;
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
