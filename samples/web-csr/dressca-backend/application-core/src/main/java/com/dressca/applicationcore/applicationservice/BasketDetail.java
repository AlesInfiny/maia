package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.displayitem.DisplayItem;
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
  Basket basket;
  List<DisplayItem> displayItems;
  List<UUID> deletedItemIds;
}
