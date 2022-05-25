package com.dressca.infrastructure.repository.jdbc;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcBasketRepository;
import com.dressca.infrastructure.repository.jdbc.internal.entity.BasketEntity;
import com.dressca.infrastructure.repository.jdbc.internal.entity.BasketItemEntity;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class BasketRepositoryImpl implements BasketRepository {

  private JdbcBasketRepository repository;

  @Override
  public Optional<Basket> findById(long id) {
    try {
      BasketEntity entity = repository.findById(id).orElseThrow();
      return Optional.of(toBasket(entity));
    } catch (NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Basket> findByBuyerId(String buyerId) {
    try {
      BasketEntity entity = repository.findFirstByBuyerId(buyerId).orElseThrow();
      return Optional.of(toBasket(entity));
    } catch (NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public Basket add(Basket basket) {
    BasketEntity entity = new BasketEntity(basket.getBuyerId());
    entity.setId(basket.getId());
    List<BasketItem> items = basket.getItems();
    Set<BasketItemEntity> itemEntities =
        items.stream().map(this::toBasketItemEntity).collect(Collectors.toSet());
    entity.setItems(itemEntities);
    BasketEntity returnEntity = repository.save(entity);
    return toBasket(returnEntity);
  }

  @Override
  public void remove(Basket basket) {
    repository.delete(new BasketEntity(basket.getBuyerId()));
  }

  @Override
  public void update(Basket basket) {
    BasketEntity entity = new BasketEntity(basket.getBuyerId());
    entity.setId(basket.getId());
    List<BasketItem> items = basket.getItems();
    Set<BasketItemEntity> itemEntities =
        items.stream().map(this::toBasketItemEntity).collect(Collectors.toSet());
    entity.setItems(itemEntities);
    repository.save(entity);
  }

  private Basket toBasket(BasketEntity entity) {
    Basket basket = new Basket(entity.getBuyerId());
    basket.setId(entity.getId());
    Set<BasketItemEntity> basketItems = entity.getItems();
    if (basketItems != null) {
      List<BasketItem> items =
          entity.getItems().stream().map(this::toBasketItem).collect(Collectors.toList());
      basket.setItems(items);
    }
    return basket;
  }

  private BasketItem toBasketItem(BasketItemEntity entity) {
    return new BasketItem(entity.getId(), entity.getBasketId(), entity.getCatalogItemId(),
        entity.getUnitPrice(), entity.getQuantity());
  }

  private BasketItemEntity toBasketItemEntity(BasketItem item) {
    BasketItemEntity entity = new BasketItemEntity();
    entity.setId(item.getId());
    entity.setBasketId(item.getBasketId());
    entity.setCatalogItemId(item.getCatalogItemId());
    entity.setUnitPrice(item.getUnitPrice());
    entity.setQuantity(item.getQuantity());
    return entity;
  }
}
