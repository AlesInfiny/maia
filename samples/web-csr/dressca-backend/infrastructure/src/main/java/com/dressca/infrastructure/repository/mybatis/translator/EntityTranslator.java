package com.dressca.infrastructure.repository.mybatis.translator;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderItemEntity;
import org.springframework.beans.BeanUtils;

/**
 * エンティティとドメインオブジェクトを相互に変換するクラスです。
 */
public class EntityTranslator {

  /**
   * {@link AssetEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link AssetEntity} オブジェクト
   * @return {@link Asset} オブジェクト
   */
  public static Asset assetEntityTranslate(AssetEntity entity) {
    Asset asset = new Asset();
    BeanUtils.copyProperties(entity, asset);
    return asset;
  }

  /**
   * {@link BasketEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link BasketEntity} オブジェクト
   * @return {@link Basket} オブジェクト
   */
  public static Basket basketEntityTranslate(BasketEntity entity) {
    Basket basket = new Basket();
    BeanUtils.copyProperties(entity, basket);
    return basket;
  }

  /**
   * ドメインオブジェクトから {@link BasketEntity} を作成します。
   * 
   * @param basket {@link Basket} オブジェクト
   * @return {@link BasketEntity} オブジェクト
   */
  public static BasketEntity createBasketEntity(Basket basket) {
    BasketEntity entity = new BasketEntity();
    BeanUtils.copyProperties(basket, entity);
    return entity;
  }

  /**
   * {@link AssetEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link AssetEntity} オブジェクト
   * @return {@link Asset} オブジェクト
   */
  public static BasketItem basketItemEntityTranslate(BasketItemEntity entity) {
    BasketItem basketItem = new BasketItem();
    BeanUtils.copyProperties(entity, basketItem);
    return basketItem;
  }

  /**
   * ドメインオブジェクトから {@link BasketItemEntity} オブジェクトを変換します。
   * 
   * @param basketItem {@link BasketItem} オブジェクト
   * @return {@link BasketItemEntity} オブジェクト
   */
  public static BasketItemEntity createBasketItemEntity(BasketItem basketItem) {
    BasketItemEntity entity = new BasketItemEntity();
    BeanUtils.copyProperties(basketItem, entity);
    return entity;
  }

  /**
   * {@link CatalogBrandEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link CatalogBrandEntity} オブジェクト
   * @return {@link CatalogBrand} オブジェクト
   */
  public static CatalogBrand catalogBrandEntityTranslate(CatalogBrandEntity entity) {
    CatalogBrand catalogBrand = new CatalogBrand();
    BeanUtils.copyProperties(entity, catalogBrand);
    return catalogBrand;
  }

  /**
   * {@link CatalogCategoryEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link CatalogCategoryEntity} オブジェクト
   * @return {@link CatalogCategory} オブジェクト
   */
  public static CatalogCategory catalogCategoryEntityTranslate(CatalogCategoryEntity entity) {
    CatalogCategory catalogCategory = new CatalogCategory();
    BeanUtils.copyProperties(entity, catalogCategory);
    return catalogCategory;
  }

  /**
   * {@link CatalogItemEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link CatalogItemEntity} オブジェクト
   * @return {@link CatalogItem} オブジェクト
   */
  public static CatalogItem catalogItemEntityTranslate(CatalogItemEntity entity) {
    CatalogItem catalogItem = new CatalogItem();
    BeanUtils.copyProperties(entity, catalogItem);
    return catalogItem;
  }

  /**
   * ドメインオブジェクトから {@link CatalogItemEntity} オブジェクトを変換します。
   * 
   * @param catalogItem {@link CatalogItem} オブジェクト
   * @return {@link CatalogItemEntity} オブジェクト
   */
  public static CatalogItemEntity createCatalogItemEntity(CatalogItem catalogItem) {
    CatalogItemEntity entity = new CatalogItemEntity();
    BeanUtils.copyProperties(catalogItem, entity);
    return entity;
  }

  /**
   * {@link CatalogItemAssetEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link CatalogItemAssetEntity} オブジェクト
   * @return {@link CatalogItemAsset} オブジェクト
   */
  public static CatalogItemAsset catalogItemAssetEntityTranslate(CatalogItemAssetEntity entity) {
    CatalogItemAsset catalogItemAsset = new CatalogItemAsset();
    BeanUtils.copyProperties(entity, catalogItemAsset);
    return catalogItemAsset;
  }

  /**
   * {@link OrderEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link OrderEntity} オブジェクト
   * @return {@link Order} オブジェクト
   */
  public static Order orderEntityTranslate(OrderEntity entity) {
    Order order = new Order();
    BeanUtils.copyProperties(entity, order);
    return order;
  }

  /**
   * {@link OrderItemEntity} をドメインオブジェクトに変換します。
   * 
   * @param entity {@link OrderItemEntity} オブジェクト
   * @return {@link OrderItem} オブジェクト
   */
  public static OrderItem orderItemEntityTranslate(OrderItemEntity entity) {
    OrderItem orderItem = new OrderItem();
    BeanUtils.copyProperties(entity, orderItem);
    return orderItem;
  }
}
