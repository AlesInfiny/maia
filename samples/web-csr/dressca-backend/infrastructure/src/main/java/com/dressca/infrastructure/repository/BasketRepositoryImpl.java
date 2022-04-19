package com.dressca.infrastructure.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcBasketRepository;
import com.dressca.infrastructure.repository.jdbc.entity.BasketEntity;

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

    @Override
    public Optional<Basket> getWithBasketItems(long basketId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Basket> getWithBasketItems(String buyerId) {
        // TODO Auto-generated method stub
        return null;
    }

    private Basket toBasket(BasketEntity entity) {
        return new Basket(entity.getBuyerId());
    }
}
