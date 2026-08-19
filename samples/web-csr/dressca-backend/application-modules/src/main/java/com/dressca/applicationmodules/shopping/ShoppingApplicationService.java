package com.dressca.applicationmodules.shopping;

import com.dressca.applicationmodules.shopping.dto.BasketDetail;
import com.dressca.applicationmodules.shopping.entity.Basket;
import com.dressca.applicationmodules.shopping.entity.BasketItem;
import com.dressca.applicationmodules.shopping.entity.DisplayItem;
import com.dressca.applicationmodules.shopping.entity.Order;
import com.dressca.applicationmodules.shopping.entity.OrderItem;
import com.dressca.applicationmodules.shopping.entity.OrderItemAsset;
import com.dressca.applicationmodules.shopping.exception.DisplayItemInBasketNotFoundException;
import com.dressca.applicationmodules.shopping.exception.DisplayItemNotFoundException;
import com.dressca.applicationmodules.shopping.exception.EmptyBasketOnCheckoutException;
import com.dressca.applicationmodules.shopping.internal.domain.DisplayItemDomainService;
import com.dressca.applicationmodules.shopping.internal.domain.constant.ShoppingMessageIdConstants;
import com.dressca.applicationmodules.shopping.internal.domain.repository.BasketRepository;
import com.dressca.applicationmodules.shopping.internal.domain.repository.DisplayItemRepository;
import com.dressca.applicationmodules.shopping.internal.domain.repository.OrderRepository;
import com.dressca.applicationmodules.shopping.valueobject.DisplayItemOrdered;
import com.dressca.applicationmodules.shopping.valueobject.ShipTo;
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
  private final DisplayItemRepository displayItemRepository;
  private final OrderRepository orderRepository;
  private final DisplayItemDomainService displayItemDomainService;
  private final AbstractStructuredLogger apLog;

  /**
   * 買い物かごに陳列品を追加します。
   *
   * @param buyerId 購入者 ID 。
   * @param displayItemId 陳列品 ID 。
   * @param quantity 数量。
   * @throws DisplayItemNotFoundException 存在しない陳列品が指定された場合。
   */
  public void addItemToBasket(UUID buyerId, UUID displayItemId, int quantity)
      throws DisplayItemNotFoundException {
    apLog.debug(messages.getMessage(ShoppingMessageIdConstants.D_SHOPPING_ADD_ITEM_TO_BASKET,
        new Object[] {buyerId, displayItemId, quantity}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    // 陳列品リポジトリに存在しない陳列品が指定されていないか確認
    if (!this.displayItemDomainService.existAll(List.of(displayItemId))) {
      throw new DisplayItemNotFoundException(displayItemId);
    }
    DisplayItem displayItem =
        this.displayItemDomainService.getExistDisplayItems(List.of(displayItemId)).get(0);

    basket.addItem(displayItemId, displayItem.getPrice(), quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かご内の陳列品の数量を設定します。
   *
   * @param buyerId 購入者 ID 。
   * @param quantities キーに陳列品 ID 、値に数量を設定した Map 。
   * @throws DisplayItemNotFoundException 存在しない陳列品が指定された場合。
   * @throws DisplayItemInBasketNotFoundException 買い物かごに存在しない陳列品が指定された場合。
   */
  public void setQuantities(UUID buyerId, Map<UUID, Integer> quantities)
      throws DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    apLog.debug(
        messages.getMessage(ShoppingMessageIdConstants.D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES,
            new Object[] {buyerId, quantities}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<UUID> displayItemIds = new ArrayList<>(quantities.keySet());

    if (!this.displayItemDomainService.existAll(displayItemIds)) {
      List<DisplayItem> deletedDisplayItems =
          this.displayItemRepository.findDeletedItemsByDisplayItemIdIn(displayItemIds);
      throw new DisplayItemNotFoundException(
          deletedDisplayItems.stream().map(DisplayItem::getId).toArray(UUID[]::new));
    }

    // 買い物かごに入っていない陳列品が指定されていないか確認
    List<UUID> notExistsInBasketDisplayIds =
        quantities.keySet().stream().filter(displayItemId -> !basket.isInDisplayItem(displayItemId))
            .collect(Collectors.toList());
    if (!notExistsInBasketDisplayIds.isEmpty()) {
      throw new DisplayItemInBasketNotFoundException(basket.getId(), notExistsInBasketDisplayIds);
    }

    for (BasketItem item : basket.getItems()) {
      Integer quantity = quantities.get(item.getDisplayItemId());
      if (quantity != null) {
        item.setQuantity(quantity);
      }
    }

    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かごから陳列品を削除します。
   *
   * @param buyerId 購入者 ID 。
   * @param displayItemId 削除対象の陳列品 ID 。
   * @throws DisplayItemNotFoundException 存在しない陳列品が指定された場合。
   * @throws DisplayItemInBasketNotFoundException 買い物かごに存在しない陳列品が指定された場合。
   */
  public void deleteItemFromBasket(UUID buyerId, UUID displayItemId)
      throws DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    apLog.debug(messages.getMessage(ShoppingMessageIdConstants.D_SHOPPING_DELETE_ITEM_FROM_BASKET,
        new Object[] {buyerId, displayItemId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);

    if (!displayItemDomainService.existDisplayItemIncludingDeleted(displayItemId)) {
      throw new DisplayItemNotFoundException(displayItemId);
    }

    BasketItem basketItem =
        basket.getItems().stream().filter(item -> item.getDisplayItemId().equals(displayItemId))
            .findFirst().orElseThrow(() -> new DisplayItemInBasketNotFoundException(basket.getId(),
                Collections.singletonList(displayItemId)));

    basketItem.setQuantity(0);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 購入者 ID に対応する買い物かごと情報とその陳列品一覧を取得します。
   *
   * @param buyerId 購入者 ID 。
   * @return 買い物かごとその陳列品一覧。
   */
  public BasketDetail getBasketDetail(UUID buyerId) {
    apLog.debug(messages.getMessage(ShoppingMessageIdConstants.D_SHOPPING_GET_BASKET_ITEMS,
        new Object[] {buyerId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<UUID> displayItemIds =
        basket.getItems().stream().map(BasketItem::getDisplayItemId).collect(Collectors.toList());
    List<DisplayItem> displayItems = new ArrayList<>();
    if (!displayItemIds.isEmpty()) {
      displayItems =
          this.displayItemRepository.findByDisplayItemIdInIncludingDeleted(displayItemIds);
    }
    List<UUID> deletedItemIds = displayItems.stream().filter(DisplayItem::isDeleted)
        .map(DisplayItem::getId).collect(Collectors.toList());
    return new BasketDetail(basket, displayItems, deletedItemIds);
  }

  /**
   * 注文を確定します。
   *
   * @param buyerId 購入者 ID 。
   * @param shipToAddress お届け先。
   * @return 作成した注文情報。
   * @throws EmptyBasketOnCheckoutException basketId に該当する買い物かごが空の場合。
   */
  public Order checkout(UUID buyerId, ShipTo shipToAddress) throws EmptyBasketOnCheckoutException {
    apLog.debug(messages.getMessage(ShoppingMessageIdConstants.D_SHOPPING_CHECKOUT,
        new Object[] {buyerId, shipToAddress}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new EmptyBasketOnCheckoutException(null);
    }

    List<UUID> displayItemIds =
        basket.getItems().stream().map(BasketItem::getDisplayItemId).collect(Collectors.toList());
    List<DisplayItem> displayItems =
        this.displayItemRepository.findByDisplayItemIdIn(displayItemIds);
    List<OrderItem> orderItems = basket.getItems().stream()
        .map(basketItems -> this.mapToOrderItem(basketItems, displayItems))
        .collect(Collectors.toList());
    Order order = new Order(basket.getBuyerId(), shipToAddress, orderItems);
    UUID orderId = order.getId();
    orderItems.forEach(item -> item.setOrderId(orderId));
    order = this.orderRepository.add(order);
    this.basketRepository.remove(basket);
    return order;
  }

  /**
   * 購入者 ID に対応する買い物かご情報を取得するか、無ければ新規作成します。
   *
   * @param buyerId 購入者 ID 。
   * @return 買い物かご情報。
   */
  private Basket getOrCreateBasketForUser(UUID buyerId) {
    if (buyerId == null) {
      throw new IllegalArgumentException("buyerIdがnull");
    }

    return this.basketRepository.findByBuyerId(buyerId).orElseGet(() -> this.createBasket(buyerId));
  }

  /**
   * 購入者 ID を指定して、買い物かごを新規で作成します。
   *
   * @param buyerId 購入者 ID 。
   * @return 買い物かご。
   */
  private Basket createBasket(UUID buyerId) {
    Basket basket = new Basket(buyerId);
    return this.basketRepository.add(basket);
  }

  /**
   * 買い物かごアイテムを注文アイテムに変換します。
   *
   * @param basketItem 買い物かごアイテム。
   * @param displayItems 陳列品のリスト。
   * @return 変換された注文アイテム。
   */
  private OrderItem mapToOrderItem(BasketItem basketItem, List<DisplayItem> displayItems) {
    DisplayItem displayItem = displayItems.stream()
        .filter(d -> d.getId().equals(basketItem.getDisplayItemId())).findFirst().orElseThrow(
            () -> new SystemException(null, CommonExceptionIdConstants.E_BUSINESS, null, null));
    DisplayItemOrdered itemOrdered = new DisplayItemOrdered(displayItem.getId(),
        displayItem.getName(), displayItem.getProductCode());
    OrderItem orderItem =
        new OrderItem(itemOrdered, basketItem.getUnitPrice(), basketItem.getQuantity());
    List<OrderItemAsset> orderItemAssets = displayItem.getAssets().stream()
        .map(asset -> new OrderItemAsset(asset.getAssetCode(), orderItem.getId()))
        .collect(Collectors.toList());
    orderItem.addAsset(orderItemAssets);

    return orderItem;
  }
}
