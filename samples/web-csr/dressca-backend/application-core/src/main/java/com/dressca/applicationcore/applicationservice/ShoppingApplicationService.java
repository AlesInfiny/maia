package com.dressca.applicationcore.applicationservice;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.MessageIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.SystemException;

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

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 買い物かごに商品を追加します。
   * 
   * @param buyerId       顧客ID
   * @param catalogItemId カタログ商品ID
   * @param quantity      数量
   * @throws CatalogNotFoundException 存在しないカタログ商品が指定された場合
   */
  public void addItemToBasket(String buyerId, long catalogItemId, int quantity)
      throws CatalogNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_BASKET0001_LOG,
        new Object[] { buyerId, catalogItemId, quantity }, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    // カタログリポジトリに存在しないカタログアイテムが指定されていないか確認
    if (!this.catalogDomainService.existAll(List.of(catalogItemId))) {
      throw new CatalogNotFoundException(catalogItemId);
    }
    CatalogItem catalogItem = this.catalogDomainService.getExistCatalogItems(List.of(catalogItemId)).get(0);

    basket.addItem(catalogItemId, catalogItem.getPrice(), quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かご内の商品の数量を設定します。
   * 
   * @param buyerId    顧客ID
   * @param quantities キーにカタログ商品ID、値に数量を設定したMap
   * @throws CatalogNotFoundException             存在しないカタログ商品が指定された場合
   * @throws CatalogItemInBasketNotFoundException 買い物かごに存在しないカタログアイテムが指定された場合
   */
  public void setQuantities(String buyerId, Map<Long, Integer> quantities)
      throws CatalogNotFoundException, CatalogItemInBasketNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_BASKET0002_LOG, new Object[] { buyerId, quantities },
        Locale.getDefault()));

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
   * 顧客IDに対応する買い物かごと情報とその商品一覧を取得します。
   * 
   * @param buyerId 顧客ID
   * @return 買い物かごとその商品一覧
   */
  public BasketDetail getBasketDetail(String buyerId) {

    apLog.debug(messages.getMessage(MessageIdConstant.D_BASKET0003_LOG, new Object[] { buyerId }, Locale.getDefault()));

    Basket basket = getOrCreateBasketForUser(buyerId);
    List<Long> catalogItemIds = basket.getItems().stream()
        .map(basketItem -> basketItem.getCatalogItemId())
        .collect(Collectors.toList());
    List<CatalogItem> catalogItems = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    return new BasketDetail(basket, catalogItems);
  }

  /**
   * 注文を確定します。
   * 
   * @param buyerId       顧客ID
   * @param shipToAddress お届け先
   * @return 作成した注文情報
   * @throws EmptyBasketOnCheckoutException basketId に該当する買い物かごが空の場合
   */
  public Order checkout(String buyerId, ShipTo shipToAddress)
      throws EmptyBasketOnCheckoutException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_BASKET0004_LOG, new Object[] { buyerId, shipToAddress },
        Locale.getDefault()));

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
   * 顧客IDに対応する買い物かご情報を取得するか、無ければ新規作成します。
   * 
   * @param buyerId 顧客ID
   * @return 買い物かご情報
   */
  private Basket getOrCreateBasketForUser(String buyerId) {
    if (StringUtils.isBlank(buyerId)) {
      throw new IllegalArgumentException("buyerIdがnullまたは空文字");
    }

    return this.basketRepository.findByBuyerId(buyerId).orElseGet(() -> this.createBasket(buyerId));
  }

  private Basket createBasket(String buyerId) {
    Basket basket = new Basket(buyerId);
    return this.basketRepository.add(basket);
  }

  private OrderItem mapToOrderItem(BasketItem basketItem, List<CatalogItem> catalogItems) {
    CatalogItem catalogItem = catalogItems.stream()
        .filter(c -> c.getId() == basketItem.getCatalogItemId()).findFirst()
        .orElseThrow(() -> new SystemException(null, ExceptionIdConstant.E_SHARE0000, null, null));
    CatalogItemOrdered itemOrdered = new CatalogItemOrdered(catalogItem.getId(),
        catalogItem.getName(), catalogItem.getProductCode());
    OrderItem orderItem = new OrderItem(itemOrdered, basketItem.getUnitPrice(), basketItem.getQuantity());
    List<OrderItemAsset> orderItemAssets = catalogItem.getAssets().stream()
        .map(asset -> new OrderItemAsset(asset.getAssetCode(), orderItem.getId()))
        .collect(Collectors.toList());
    orderItem.addAsset(orderItemAssets);

    return orderItem;
  }
}
