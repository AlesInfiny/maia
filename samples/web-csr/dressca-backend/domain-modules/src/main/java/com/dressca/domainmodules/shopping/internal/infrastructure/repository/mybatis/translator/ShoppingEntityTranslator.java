package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.translator;

import com.dressca.domainmodules.common.mybatis.generated.entity.BasketEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.BasketItemEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.OrderEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.OrderItemEntity;
import com.dressca.domainmodules.shopping.models.Basket;
import com.dressca.domainmodules.shopping.models.BasketItem;
import com.dressca.domainmodules.shopping.models.DisplayItemBrand;
import com.dressca.domainmodules.shopping.models.DisplayItemCategory;
import com.dressca.domainmodules.shopping.models.Order;
import com.dressca.domainmodules.shopping.models.OrderItem;
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
   * テーブルエンティティ： {@link CatalogBrandEntity} をエンティティ： {@link DisplayItemBrand} に変換します。
   * 陳列アイテムブランド専用テーブルを持たないため、カタログブランドを源泉に陳列アイテムブランドを構築します。
   *
   * @param entity {@link CatalogBrandEntity} オブジェクト。
   * @return {@link DisplayItemBrand} オブジェクト。
   */
  public static DisplayItemBrand displayItemBrandEntityTranslate(CatalogBrandEntity entity) {
    DisplayItemBrand displayItemBrand = new DisplayItemBrand();
    BeanUtils.copyProperties(entity, displayItemBrand);
    return displayItemBrand;
  }

  /**
   * テーブルエンティティ： {@link CatalogCategoryEntity} をエンティティ： {@link DisplayItemCategory} に変換します。
   * 陳列品カテゴリ専用テーブルを持たないため、カタログカテゴリを源泉に陳列品カテゴリを構築します。
   *
   * @param entity {@link CatalogCategoryEntity} オブジェクト。
   * @return {@link DisplayItemCategory} オブジェクト。
   */
  public static DisplayItemCategory displayItemCategoryEntityTranslate(
      CatalogCategoryEntity entity) {
    DisplayItemCategory displayItemCategory = new DisplayItemCategory();
    BeanUtils.copyProperties(entity, displayItemCategory);
    return displayItemCategory;
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
