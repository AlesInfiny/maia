package com.dressca.applicationcore.applicationservice;

import java.util.List;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.catalog.CatalogItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 買い物かごの詳細を格納するクラスです。
 */
@AllArgsConstructor
@Getter
public class BasketDetail {
  Basket basket;
  List<CatalogItem> catalogItems;
}
