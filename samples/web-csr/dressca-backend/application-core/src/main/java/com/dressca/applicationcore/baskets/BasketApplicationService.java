package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 買い物かご情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
public class BasketApplicationService {
  private BasketRepository basketRepository;

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
}
