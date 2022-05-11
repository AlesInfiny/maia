package com.dressca.infrastructure.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcBasketRepository;
import com.dressca.infrastructure.repository.jdbc.entity.BasketEntity;
import com.dressca.infrastructure.repository.jdbc.entity.BasketItemEntity;
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
            BasketEntity entity = repository.findByBuyerId(buyerId).orElseThrow();
            return Optional.of(toBasket(entity));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Basket add(Basket basket) {
        BasketEntity entity = repository.save(new BasketEntity(basket.getBuyerId()));
        return toBasket(entity);
    }

    @Override
    public void remove(Basket basket) {
        repository.delete(new BasketEntity(basket.getBuyerId()));
    }

    @Override
    public void update(Basket basket) {
        repository.save(new BasketEntity(basket.getBuyerId()));
    }

    private Basket toBasket(BasketEntity entity) {
        Basket basket = new Basket(entity.getBuyerId());
        basket.setId(entity.getId());
        List<BasketItem> items =
                entity.getItems().stream().map(this::toBasketItem).collect(Collectors.toList());
        basket.setItems(items);
        return basket;
    }

    private BasketItem toBasketItem(BasketItemEntity entity) {
        return new BasketItem(entity.getId(), entity.getBasketId(), entity.getCatalogItemId(),
                entity.getUnitPrice(), entity.getQuantity());
    }
}
