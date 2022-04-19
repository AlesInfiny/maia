package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dressca.applicationcore.accounting.Account;
import com.dressca.applicationcore.accounting.AccountItem;

import lombok.Data;
import lombok.NonNull;

@Data
public class Basket {
    private List<BasketItem> items = List.of();
    @NonNull
    private String buyerId;
    
    public void addItem(long catalogItemId, BigDecimal unitPrice, int quantity) {
        Optional<BasketItem> existingItem = this.items.stream()
            .filter(item -> item.getCatalogItemId() == catalogItemId)
            .findFirst();
        
        existingItem.ifPresentOrElse(
            item -> item.addQuantity(quantity), 
            () -> this.items.add(new BasketItem(0, catalogItemId, unitPrice, quantity))
        );
    }

    public void removeEmptyItems() {
        this.items.removeIf(item -> item.getBasketId() == 0);
    }

    public boolean isInCatalogItem(long catalogItemId) {
        return this.items.stream()
            .anyMatch(item -> item.getCatalogItemId() == catalogItemId);
    }

    public Account getAccount() {
        List<AccountItem> accountItems = this.items.stream()
            .map(basketItem -> new AccountItem(basketItem.getQuantity(), basketItem.getUnitPrice()))
            .collect(Collectors.toList());
        return new Account(accountItems);
    }
}
