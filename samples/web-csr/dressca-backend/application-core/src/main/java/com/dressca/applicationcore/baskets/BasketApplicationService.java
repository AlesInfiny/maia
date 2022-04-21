package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BasketApplicationService {
    private BasketRepository basketRepository;

    public void addItemToBasket(long basketId, long catalogItemId, BigDecimal price, int quantity)
        throws BasketNotFoundException {
        Basket basket = this.basketRepository.findById(basketId)
            .orElseThrow(() -> new BasketNotFoundException(basketId));

        basket.addItem(catalogItemId, price, quantity);
        basket.removeEmptyItems();
        this.basketRepository.update(basket);
    }

    public void deleteBasket(long basketId) throws BasketNotFoundException {
        Basket basket = this.basketRepository.getWithBasketItems(basketId)
            .orElseThrow(() -> new BasketNotFoundException(basketId));
        
        this.basketRepository.remove(basket);
    }

    public void setQuantities(long basketId, Map<Long, Integer> quantities) throws BasketNotFoundException {
        Basket basket = this.basketRepository.getWithBasketItems(basketId)
            .orElseThrow(() -> new BasketNotFoundException(basketId));
        
        for (BasketItem item : basket.getItems()) {
            Integer quantity = quantities.get(item.getCatalogItemId());
            if (quantity != null) {
                item.setQuantity(quantity);
            }
        }

        basket.removeEmptyItems();
        this.basketRepository.update(basket);
    }

    public Basket getOrCreateBasketForUser(String buyerId) {
        if(StringUtils.isBlank(buyerId)) {
            throw new IllegalArgumentException("buyerIdがnull");
        }

        return this.basketRepository.getWithBasketItems(buyerId)
            .orElseGet(() -> this.createBasket(buyerId));
    }

    private Basket createBasket(String buyerId) {
        Basket basket = new Basket(buyerId);
        return this.basketRepository.add(basket);
    }
}
