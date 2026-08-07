package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.translator;

import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.BasketEntity;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.BasketItemEntity;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.OrderEntity;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.OrderItemEntity;
import com.dressca.domainmodules.shopping.model.Basket;
import com.dressca.domainmodules.shopping.model.BasketItem;
import com.dressca.domainmodules.shopping.model.Order;
import com.dressca.domainmodules.shopping.model.OrderItem;
import org.springframework.beans.BeanUtils;

/**
 * 購買の文脈において、テーブルエンティティとエンティティを相互に変換するクラスです。
 */
public class ShoppingEntityTranslator {

  /**
   * テーブルエンティティ： {@link BasketEntity} をエンティティ： {@link Basket} に変換します。
   *
   * @param entity {@link BasketEntity} オブジェクト。
   * @return {@link Basket} オブジェクト。
   */
  public static Basket basketEntityTranslate(BasketEntity entity) {
    Basket basket = new Basket();
    BeanUtils.copyProperties(entity, basket);
    return basket;
  }

  /**
   * エンティティ： {@link Basket} をテーブルエンティティ： {@link BasketEntity} に変換します。
   *
   * @param basket {@link Basket} オブジェクト。
   * @return {@link BasketEntity} オブジェクト。
   */
  public static BasketEntity createBasketEntity(Basket basket) {
    BasketEntity entity = new BasketEntity();
    BeanUtils.copyProperties(basket, entity);
    return entity;
  }

  /**
   * テーブルエンティティ： {@link BasketItemEntity} をエンティティ： {@link BasketItem} に変換します。
   *
   * @param entity {@link BasketItemEntity} オブジェクト。
   * @return {@link BasketItem} オブジェクト。
   */
  public static BasketItem basketItemEntityTranslate(BasketItemEntity entity) {
    BasketItem basketItem = new BasketItem();
    BeanUtils.copyProperties(entity, basketItem);
    return basketItem;
  }

  /**
   * エンティティ： {@link BasketItem} をテーブルエンティティ： {@link BasketItemEntity} に変換します。
   *
   * @param basketItem {@link BasketItem} オブジェクト。
   * @return {@link BasketItemEntity} オブジェクト。
   */
  public static BasketItemEntity createBasketItemEntity(BasketItem basketItem) {
    BasketItemEntity entity = new BasketItemEntity();
    BeanUtils.copyProperties(basketItem, entity);
    return entity;
  }

  /**
   * テーブルエンティティ： {@link OrderEntity} をエンティティ： {@link Order} に変換します。
   *
   * @param entity {@link OrderEntity} オブジェクト。
   * @return {@link Order} オブジェクト。
   */
  public static Order orderEntityTranslate(OrderEntity entity) {
    Order order = new Order();
    BeanUtils.copyProperties(entity, order);
    return order;
  }

  /**
   * テーブルエンティティ： {@link OrderItemEntity} をエンティティ： {@link OrderItem} に変換します。
   *
   * @param entity {@link OrderItemEntity} オブジェクト。
   * @return {@link OrderItem} オブジェクト。
   */
  public static OrderItem orderItemEntityTranslate(OrderItemEntity entity) {
    OrderItem orderItem = new OrderItem();
    BeanUtils.copyProperties(entity, orderItem);
    return orderItem;
  }
}
