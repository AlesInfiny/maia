package com.dressca.applicationcore.applicationservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.baskets.DisplayItemInBasketNotFoundException;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.displayitem.DisplayDomainService;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayItemNotFoundException;
import com.dressca.applicationcore.displayitem.DisplayRepository;
import com.dressca.applicationcore.order.DisplayItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.RequiredArgsConstructor;

/**
 * 買い物かご情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ShoppingApplicationService {

  private final MessageSource messages;
  private final BasketRepository basketRepository;
  private final DisplayRepository displayRepository;
  private final OrderRepository orderRepository;
  private final DisplayDomainService displayDomainService;
  private final AbstractStructuredLogger apLog;

  /**
   * 買い物かごに商品を追加します。
   * 
   * @param buyerId 購入者 ID 。
   * @param displayItemId 掲載品 ID 。
   * @param quantity 数量。
   * @throws DisplayItemNotFoundException 存在しない掲載品が指定された場合。
   */
  public void addItemToBasket(String buyerId, long displayItemId, int quantity)
      throws DisplayItemNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_ADD_ITEM_TO_BASKET,
        new Object[] {buyerId, displayItemId, quantity}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    // 掲載品リポジトリに存在しない掲載品が指定されていないか確認
    if (!this.displayDomainService.existAll(List.of(displayItemId))) {
      throw new DisplayItemNotFoundException(displayItemId);
    }
    DisplayItem displayItem =
        this.displayDomainService.getExistDisplayItems(List.of(displayItemId)).get(0);

    basket.addItem(displayItemId, displayItem.getPrice(), quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かご内の商品の数量を設定します。
   * 
   * @param buyerId 購入者 ID 。
   * @param quantities キーに掲載品 ID 、値に数量を設定した Map 。
   * @throws DisplayItemNotFoundException 存在しない掲載品が指定された場合。
   * @throws DisplayItemInBasketNotFoundException 買い物かごに存在しない掲載品が指定された場合。
   */
  public void setQuantities(String buyerId, Map<Long, Integer> quantities)
      throws DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES,
        new Object[] {buyerId, quantities}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);

    List<Long> displayItemIds = new ArrayList<>(quantities.keySet());

    if (!this.displayDomainService.existAll(displayItemIds)) {
      List<DisplayItem> deletedDisplayItems =
          this.displayRepository.findDeletedItemsByDisplayItemIdIn(displayItemIds);
      throw new DisplayItemNotFoundException(deletedDisplayItems.stream().map(DisplayItem::getId)
          .mapToLong(Long::longValue).toArray());
    }

    // 買い物かごに入っていない掲載品が指定されていないか確認
    List<Long> notExistsInBasketDisplayIds =
        quantities.keySet().stream().filter(displayItemId -> !basket.isInDisplayItem(displayItemId))
            .collect(Collectors.toList());
    if (!notExistsInBasketDisplayIds.isEmpty()) {
      throw new DisplayItemInBasketNotFoundException(notExistsInBasketDisplayIds, basket.getId());
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
   * 買い物かごから商品を削除します。
   * 
   * @param buyerId 購入者 ID 。
   * @param displayItemId 削除対象の掲載品の ID 。
   * @throws DisplayItemNotFoundException 存在しない掲載品が指定された場合。
   * @throws DisplayItemInBasketNotFoundException 買い物かごに存在しない掲載品が指定された場合。
   */
  public void deleteItemFromBasket(String buyerId, long displayItemId)
      throws DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_DELETE_ITEM_FROM_BASKET,
        new Object[] {buyerId, displayItemId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);

    if (!displayDomainService.existDisplayItemIncludingDeleted(displayItemId)) {
      throw new DisplayItemNotFoundException(displayItemId);
    }

    BasketItem basketItem =
        basket.getItems().stream().filter(item -> item.getDisplayItemId() == displayItemId)
            .findFirst().orElseThrow(() -> new DisplayItemInBasketNotFoundException(
                Collections.singletonList(Long.valueOf(displayItemId)), basket.getId()));

    basketItem.setQuantity(0);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 購入者 ID に対応する買い物かごと情報とその商品一覧を取得します。
   * 
   * @param buyerId 購入者 ID 。
   * @return 買い物かごとその商品一覧。
   */
  public BasketDetail getBasketDetail(String buyerId) {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_GET_BASKET_ITEMS,
        new Object[] {buyerId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<Long> displayItemIds =
        basket.getItems().stream().map(BasketItem::getDisplayItemId).collect(Collectors.toList());
    List<DisplayItem> displayItems = new ArrayList<DisplayItem>();
    if (!displayItemIds.isEmpty()) {
      displayItems = this.displayRepository.findByDisplayItemIdInIncludingDeleted(displayItemIds);
    }
    List<Long> deletedItemIds = displayItems.stream().filter(DisplayItem::isDeleted)
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
  public Order checkout(String buyerId, ShipTo shipToAddress)
      throws EmptyBasketOnCheckoutException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_CHECKOUT,
        new Object[] {buyerId, shipToAddress}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new EmptyBasketOnCheckoutException(null);
    }

    List<Long> displayItemIds =
        basket.getItems().stream().map(BasketItem::getDisplayItemId).collect(Collectors.toList());
    List<DisplayItem> displayItems = this.displayRepository.findByDisplayItemIdIn(displayItemIds);
    List<OrderItem> orderItems = basket.getItems().stream()
        .map(basketItems -> this.mapToOrderItem(basketItems, displayItems))
        .collect(Collectors.toList());
    Order order = new Order(basket.getBuyerId(), shipToAddress, orderItems);
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
  private Basket getOrCreateBasketForUser(String buyerId) {
    if (StringUtils.isBlank(buyerId)) {
      throw new IllegalArgumentException("buyerIdがnullまたは空文字");
    }

    return this.basketRepository.findByBuyerId(buyerId).orElseGet(() -> this.createBasket(buyerId));
  }

  /**
   * 購入者 ID を指定して、買い物かごを新規で作成します。
   * 
   * @param buyerId 購入者 ID 。
   * @return 買い物かご。
   */
  private Basket createBasket(String buyerId) {
    Basket basket = new Basket(buyerId);
    return this.basketRepository.add(basket);
  }

  /**
   * 買い物かごアイテムを注文アイテムに変換します。
   * 
   * @param basketItem 買い物かごアイテム。
   * @param catalogItems カタログアイテムのリスト。
   * @return 変換された注文アイテム。
   */
  private OrderItem mapToOrderItem(BasketItem basketItem, List<DisplayItem> displayItems) {
    DisplayItem displayItem = displayItems.stream()
        .filter(d -> d.getId() == basketItem.getDisplayItemId()).findFirst().orElseThrow(
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
