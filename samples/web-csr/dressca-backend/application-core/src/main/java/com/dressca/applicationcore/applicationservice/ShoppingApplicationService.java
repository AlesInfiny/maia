package com.dressca.applicationcore.applicationservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import lombok.AllArgsConstructor;

/**
 * 買い物かご情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ShoppingApplicationService {

  @Autowired
  private MessageSource messages;

  private BasketRepository basketRepository;
  private CatalogRepository catalogRepository;
  private OrderRepository orderRepository;
  private CatalogDomainService catalogDomainService;

  @Autowired
  private AbstractStructuredLogger apLog;

  /**
   * 買い物かごに商品を追加します。
   * 
   * @param buyerId 購入者 ID 。
   * @param catalogItemId カタログアイテム ID 。
   * @param quantity 数量。
   * @throws CatalogNotFoundException 存在しないカタログアイテムが指定された場合。
   */
  public void addItemToBasket(String buyerId, long catalogItemId, int quantity)
      throws CatalogNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_ADD_ITEM_TO_BASKET,
        new Object[] {buyerId, catalogItemId, quantity}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    // カタログリポジトリに存在しないカタログアイテムが指定されていないか確認
    if (!this.catalogDomainService.existAll(List.of(catalogItemId))) {
      throw new CatalogNotFoundException(catalogItemId);
    }
    CatalogItem catalogItem =
        this.catalogDomainService.getExistCatalogItems(List.of(catalogItemId)).get(0);

    basket.addItem(catalogItemId, catalogItem.getPrice(), quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かご内の商品の数量を設定します。
   * 
   * @param buyerId 購入者 ID 。
   * @param quantities キーにカタログアイテム ID 、値に数量を設定した Map 。
   * @throws CatalogNotFoundException 存在しないカタログアイテムが指定された場合。
   * @throws CatalogItemInBasketNotFoundException 買い物かごに存在しないカタログアイテムが指定された場合。
   */
  public void setQuantities(String buyerId, Map<Long, Integer> quantities)
      throws CatalogNotFoundException, CatalogItemInBasketNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_SET_BASKET_ITEMS_QUANTITIES,
        new Object[] {buyerId, quantities}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    // カタログリポジトリに存在しないカタログアイテムが指定されていないか確認
    if (!this.catalogDomainService.existAll(List.copyOf(quantities.keySet()))) {
      throw new CatalogNotFoundException();
    }

    // 買い物かごに入っていないカタログアイテムが指定されていないか確認
    List<Long> notExistsInBasketCatalogIds = quantities.keySet().stream()
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

  /**
   * 買い物かごから商品を削除します。
   * 
   * @param buyerId 購入者 ID 。
   * @param catalogItemId 削除対象のカタログアイテムの ID 。
   * @throws CatalogNotFoundException 存在しないカタログアイテムが指定された場合。
   * @throws CatalogItemInBasketNotFoundException 買い物かごに存在しないカタログアイテムが指定された場合。
   */
  public void deleteItemFromBasket(String buyerId, long catalogItemId)
      throws CatalogNotFoundException, CatalogItemInBasketNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_SHOPPING_DELETE_ITEM_FROM_BASKET,
        new Object[] {buyerId, catalogItemId}, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);

    if (!catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId)) {
      throw new CatalogNotFoundException();
    }

    BasketItem basketItem = basket.getItems().stream()
        .filter(item -> item.getCatalogItemId() == catalogItemId)
        .findFirst()
        .orElseThrow(() -> new CatalogItemInBasketNotFoundException(
            Collections.singletonList(Long.valueOf(catalogItemId)), basket.getId()));

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
    List<Long> catalogItemIds = basket.getItems().stream()
        .map(BasketItem::getCatalogItemId)
        .collect(Collectors.toList());
    List<CatalogItem> catalogItems = new ArrayList<CatalogItem>();
    if (!catalogItemIds.isEmpty()) {
      catalogItems = this.catalogRepository.findByCatalogItemIdInIncludingDeleted(catalogItemIds);
    }
    List<Long> deletedItemIds = catalogItems.stream()
        .filter(CatalogItem::isDeleted)
        .map(CatalogItem::getId)
        .collect(Collectors.toList());
    return new BasketDetail(basket, catalogItems, deletedItemIds);
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

    List<Long> catalogItemIds = basket.getItems().stream().map(BasketItem::getCatalogItemId)
        .collect(Collectors.toList());
    List<CatalogItem> catalogItems = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<OrderItem> orderItems = basket.getItems().stream()
        .map(basketItems -> this.mapToOrderItem(basketItems, catalogItems))
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
  private OrderItem mapToOrderItem(BasketItem basketItem, List<CatalogItem> catalogItems) {
    CatalogItem catalogItem = catalogItems.stream()
        .filter(c -> c.getId() == basketItem.getCatalogItemId()).findFirst()
        .orElseThrow(
            () -> new SystemException(null, CommonExceptionIdConstants.E_BUSINESS, null, null));
    CatalogItemOrdered itemOrdered = new CatalogItemOrdered(catalogItem.getId(),
        catalogItem.getName(), catalogItem.getProductCode());
    OrderItem orderItem =
        new OrderItem(itemOrdered, basketItem.getUnitPrice(), basketItem.getQuantity());
    List<OrderItemAsset> orderItemAssets = catalogItem.getAssets().stream()
        .map(asset -> new OrderItemAsset(asset.getAssetCode(), orderItem.getId()))
        .collect(Collectors.toList());
    orderItem.addAsset(orderItemAssets);

    return orderItem;
  }
}
