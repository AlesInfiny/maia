package com.dressca.applicationcore.baskets;

import java.util.Optional;

public interface BasketRepository {
    Optional<Basket> findById(long id);
    Basket add(Basket basket);
    void remove(Basket basket);
    void update(Basket basket);
    Optional<Basket> getWithBasketItems(long basketId);
    Optional<Basket> getWithBasketItems(String buyerId);
}
