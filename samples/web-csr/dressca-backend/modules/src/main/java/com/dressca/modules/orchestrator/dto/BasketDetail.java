package com.dressca.modules.orchestrator.dto;

import java.util.List;
import com.dressca.modules.baskets.Basket;
import com.dressca.modules.catalog.CatalogItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 買い物かごの詳細を格納するクラスです。
 */
@AllArgsConstructor
@Getter
public class BasketDetail {
  private Basket basket;
  private List<CatalogItem> catalogItems;
  private List<Long> deletedItemIds;
}
