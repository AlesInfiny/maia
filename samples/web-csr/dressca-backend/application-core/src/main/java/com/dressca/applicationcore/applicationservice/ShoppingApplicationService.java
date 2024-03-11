package com.dressca.applicationcore.applicationservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.SystemException;
import lombok.AllArgsConstructor;

/**
 * 買い物かご情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ShoppingApplicationService {
  private BasketRepository basketRepository;
  private CatalogRepository catalogRepository;
  private OrderRepository orderRepository;

  /**
   * 買い物かごに商品を追加します。
   * 
   * @param basketId      買い物かごID
   * @param catalogItemId カタログ商品ID
   * @param price         単価
   * @param quantity      数量
   * @throws BasketNotFoundException 買い物かごが見つからなかった場合
   */
  public void addItemToBasket(long basketId, long catalogItemId, BigDecimal price, int quantity)
      throws BasketNotFoundException {
    Basket basket = this.basketRepository.findById(basketId)
        .orElseThrow(() -> new BasketNotFoundException(basketId));

    basket.addItem(catalogItemId, price, quantity);
    basket.removeEmptyItems();
    this.basketRepository.update(basket);
  }

  /**
   * 買い物かごを削除します。
   * 
   * @param basketId 買い物かごID
   * @throws BasketNotFoundException 買い物かごが見つからなかった場合
   */
  public void deleteBasket(long basketId) throws BasketNotFoundException {
    Basket basket = this.basketRepository.findById(basketId)
        .orElseThrow(() -> new BasketNotFoundException(basketId));

    this.basketRepository.remove(basket);
  }

  /**
   * 買い物かご内の商品の数量を設定します。
   * 
   * @param basketId   買い物かごID
   * @param quantities キーにカタログ商品ID、値に数量を設定したMap
   * @throws BasketNotFoundException 買い物かごが見つからなかった場合
   */
  public void setQuantities(long basketId, Map<Long, Integer> quantities)
      throws BasketNotFoundException {
    Basket basket = this.basketRepository.findById(basketId)
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

  /**
   * 顧客IDに対応する買い物かご情報を取得するか、無ければ新規作成します。
   * 
   * @param buyerId 顧客ID
   * @return 買い物かご情報
   */
  public Basket getOrCreateBasketForUser(String buyerId) {
    if (StringUtils.isBlank(buyerId)) {
      throw new IllegalArgumentException("buyerIdがnullまたは空文字");
    }

    return this.basketRepository.findByBuyerId(buyerId).orElseGet(() -> this.createBasket(buyerId));
  }

  private Basket createBasket(String buyerId) {
    Basket basket = new Basket(buyerId);
    return this.basketRepository.add(basket);
  }

  /**
   * 注文を確定します。
   * 
   * @param basketId      買い物かご Id.
   * @param shipToAddress お届け先.
   * @return 作成した注文情報.
   * @throws BasketNotFoundException        basketId に該当する買い物かごが存在しない場合.
   * @throws EmptyBasketOnCheckoutException basketId に該当する買い物かごが空の場合.
   */
  public Order checkout(long basketId, ShipTo shipToAddress)
      throws BasketNotFoundException, EmptyBasketOnCheckoutException {
    Basket basket = this.basketRepository.findById(basketId)
        .orElseThrow(() -> new BasketNotFoundException(basketId));
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

    return this.orderRepository.add(order);
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
