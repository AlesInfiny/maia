package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.baskets.CatalogItemInBasketNotFoundException;
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 買い物かご情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ShoppingApplicationService {

  private final MessageSource messages;
  private final BasketRepository basketRepository;
  private final CatalogRepository catalogRepository;
  private final OrderRepository orderRepository;
  private final CatalogDomainService catalogDomainService;
  private final AbstractStructuredLogger apLog;

  public void addItemToBasket(UUID buyerId, UUID catalogItemId, int quantity)
      throws CatalogNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_ADD_ITEM_TO_BASKET,
        new Object[] {buyerId, catalogItemId, quantity}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    if (!this.catalogDomainService.existAll(List.of(catalogItemId))) {
      throw new CatalogNotFoundException(catalogItemId);
    }
    CatalogItem catalogItem = this.catalogDomainService.getExistCatalogItems(List.of(catalogItemId))
        .get(0);

    basket.addItem(catalogItemId, catalogItem.getPrice(), quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  public void setQuantities(UUID buyerId, Map<UUID, Integer> quantities)
      throws CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES,
        new Object[] {buyerId, quantities}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<UUID> catalogItemIds = new ArrayList<>(quantities.keySet());

    if (!this.catalogDomainService.existAll(catalogItemIds)) {
      List<CatalogItem> deletedCatalogItems =
          this.catalogRepository.findDeletedItemsByCatalogItemIdIn(catalogItemIds);
      throw new CatalogNotFoundException(deletedCatalogItems.stream().map(CatalogItem::getId)
          .toArray(UUID[]::new));
    }

    List<UUID> notExistsInBasketCatalogIds = quantities.keySet().stream()
        .filter(catalogItemId -> !basket.isInCatalogItem(catalogItemId))
        .collect(Collectors.toList());
    if (!notExistsInBasketCatalogIds.isEmpty()) {
      throw new CatalogItemInBasketNotFoundException(notExistsInBasketCatalogIds, basket.getId());
    }

    for (BasketItem item : basket.getItems()) {
      Integer quantity = quantities.get(item.getCatalogItemId());
      if (quantity != null) {
        item.setQuantity(quantity);
      }
    }

    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  public void deleteItemFromBasket(UUID buyerId, UUID catalogItemId)
      throws CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_DELETE_ITEM_FROM_BASKET,
        new Object[] {buyerId, catalogItemId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);

    if (!catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId)) {
      throw new CatalogNotFoundException(catalogItemId);
    }

    BasketItem basketItem = basket.getItems().stream()
        .filter(item -> item.getCatalogItemId().equals(catalogItemId)).findFirst()
        .orElseThrow(() -> new CatalogItemInBasketNotFoundException(
            Collections.singletonList(catalogItemId), basket.getId()));

    basketItem.setQuantity(0);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  public BasketDetail getBasketDetail(UUID buyerId) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_GET_BASKET_ITEMS,
        new Object[] {buyerId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<UUID> catalogItemIds = basket.getItems().stream().map(BasketItem::getCatalogItemId)
        .collect(Collectors.toList());
    List<CatalogItem> catalogItems = new ArrayList<>();
    if (!catalogItemIds.isEmpty()) {
      catalogItems = this.catalogRepository.findByCatalogItemIdInIncludingDeleted(catalogItemIds);
    }
    List<UUID> deletedItemIds = catalogItems.stream().filter(CatalogItem::isDeleted)
        .map(CatalogItem::getId).collect(Collectors.toList());
    return new BasketDetail(basket, catalogItems, deletedItemIds);
  }

  public Order checkout(UUID buyerId, ShipTo shipToAddress)
      throws EmptyBasketOnCheckoutException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_CHECKOUT,
        new Object[] {buyerId, shipToAddress}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new EmptyBasketOnCheckoutException(null);
    }

    List<UUID> catalogItemIds = basket.getItems().stream().map(BasketItem::getCatalogItemId)
        .collect(Collectors.toList());
    List<CatalogItem> catalogItems = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<OrderItem> orderItems = basket.getItems().stream()
        .map(basketItems -> this.mapToOrderItem(basketItems, catalogItems))
        .collect(Collectors.toList());
    Order order = new Order(basket.getBuyerId(), shipToAddress, orderItems);
    UUID orderId = order.getId();
    orderItems.forEach(item -> item.setOrderId(orderId));
    order = this.orderRepository.add(order);
    this.basketRepository.remove(basket);
    return order;
  }

  private Basket getOrCreateBasketForUser(UUID buyerId) {
    if (buyerId == null || StringUtils.isBlank(buyerId.toString())) {
      throw new IllegalArgumentException("buyerIdがnullまたは空文字");
    }

    return this.basketRepository.findByBuyerId(buyerId).orElseGet(() -> this.createBasket(buyerId));
  }

  private Basket createBasket(UUID buyerId) {
    Basket basket = new Basket(buyerId);
    return this.basketRepository.add(basket);
  }

  private OrderItem mapToOrderItem(BasketItem basketItem, List<CatalogItem> catalogItems) {
    CatalogItem catalogItem = catalogItems.stream()
        .filter(c -> c.getId().equals(basketItem.getCatalogItemId())).findFirst().orElseThrow(
            () -> new SystemException(null, CommonExceptionIdConstants.E_BUSINESS, null, null));
    CatalogItemOrdered itemOrdered = new CatalogItemOrdered(catalogItem.getId(),
        catalogItem.getName(), catalogItem.getProductCode());
    OrderItem orderItem = new OrderItem(itemOrdered, basketItem.getUnitPrice(),
        basketItem.getQuantity());
    List<OrderItemAsset> orderItemAssets = catalogItem.getAssets().stream()
        .map(asset -> new OrderItemAsset(asset.getAssetCode(), orderItem.getId()))
        .collect(Collectors.toList());
    orderItem.addAsset(orderItemAssets);

    return orderItem;
  }
}
