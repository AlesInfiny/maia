package com.dressca.applicationcore.order;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.SystemException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 注文に関連するビジネスユースケースを実現する Applicaiton Service です.
 */
@Service
@AllArgsConstructor
public class OrderApplicationService {
  private OrderRepository orderRepository;
  private BasketRepository basketRepository;
  private CatalogRepository CatalogRepository;

  /**
   * 注文を作成します.
   * 
   * @param basketId 買い物かご Id.
   * @param shipToAddress お届け先.
   * @return 作成した注文情報.
   * @throws BasketNotFoundException basketId に該当する買い物かごが存在しない場合.
   * @throws EmptyBasketOnCheckoutException basketId に該当する買い物かごが空の場合.
   */
  public Order createOrder(long basketId, ShipTo shipToAddress)
      throws BasketNotFoundException, EmptyBasketOnCheckoutException {
    Basket basket = this.basketRepository.findById(basketId)
        .orElseThrow(() -> new BasketNotFoundException(basketId));
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new EmptyBasketOnCheckoutException(null);
    }

    List<Long> catalogItemIds =
        basket.getItems().stream().map(BasketItem::getCatalogItemId).collect(Collectors.toList());
    List<CatalogItem> catalogItems = this.CatalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<OrderItem> orderItems = basket.getItems().stream()
        .map(basketItems -> this.mapToOrderItem(basketItems, catalogItems))
        .collect(Collectors.toList());
    Order order = new Order(basket.getBuyerId(), shipToAddress);
    order.setOrderItems(orderItems);
    
    return this.orderRepository.add(order);
  }

  /**
   * 指定した注文 Id 、購入者 Id の注文情報を取得します.
   * 
   * @param orderId 注文 Id.
   * @param buyerId 購入者 Id.
   * @return 注文情報.
   * @throws OrderNotFoundException 注文情報が見つからない場合.
   */
  public Order getOrder(long orderId, String buyerId) throws OrderNotFoundException {
    Order order = this.orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(null, orderId, buyerId));
    if (order.getBuyerId() != buyerId) {
      throw new OrderNotFoundException(null, orderId, buyerId);
    }

    return order;
  }

  private OrderItem mapToOrderItem(BasketItem basketItem, List<CatalogItem> catalogItems) {
    CatalogItem catalogItem = catalogItems.stream()
        .filter(c -> c.getId() == basketItem.getCatalogItemId()).findFirst()
        .orElseThrow(() -> new SystemException(null, ExceptionIdConstant.E_SHAR0000, null, null));
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
