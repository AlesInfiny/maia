package com.dressca.applicationcore.baskets;

import java.util.Optional;

/**
 * 買い物かごリポジトリ。
 */
public interface BasketRepository {
  Optional<Basket> findById(long id);

  Optional<Basket> findByBuyerId(String buyerId);

  Basket add(Basket basket);

  void remove(Basket basket);

  void update(Basket basket);
}
