package com.dressca.infrastructure.repository.mybatis.translater;

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

public class EntityTranslator {
  public static Asset assetEntityTranslate(AssetEntity entity) {
    Asset asset = new Asset();
    BeanUtils.copyProperties(entity, asset);
    return asset;
  }

  public static Basket basketEntityTranslate(BasketEntity entity) {
    Basket basket = new Basket();
    BeanUtils.copyProperties(entity, basket);
    return basket;
  }

  public static BasketEntity createBasketEntity(Basket basket) {
    BasketEntity entity = new BasketEntity();
    BeanUtils.copyProperties(basket, entity);
    return entity;
  }

  public static BasketItem basketItemEntityTranslate(BasketItemEntity entity) {
    BasketItem basketItem = new BasketItem();
    BeanUtils.copyProperties(entity, basketItem);
    return basketItem;
  }

  public static BasketItemEntity createBasketItemEntity(BasketItem basketItem) {
    BasketItemEntity entity = new BasketItemEntity();
    BeanUtils.copyProperties(basketItem, entity);
    return entity;
  }
  
  public static CatalogBrand catalogBrandEntityTranslate(CatalogBrandEntity entity) {
    CatalogBrand catalogBrand = new CatalogBrand();
    BeanUtils.copyProperties(entity, catalogBrand);
    return catalogBrand;
  }

  public static CatalogCategory catalogCategoryEntityTranslate(CatalogCategoryEntity entity) {
    CatalogCategory catalogCategory = new CatalogCategory();
    BeanUtils.copyProperties(entity, catalogCategory);
    return catalogCategory;
  }

  public static CatalogItem catalogItemEntityTranslate(CatalogItemEntity entity) {
    CatalogItem catalogItem = new CatalogItem();
    BeanUtils.copyProperties(entity, catalogItem);
    return catalogItem;
  }

  public static CatalogItemAsset catalogItemAssetEntityTranslate(CatalogItemAssetEntity entity) {
    CatalogItemAsset catalogItemAsset = new CatalogItemAsset();
    BeanUtils.copyProperties(entity, catalogItemAsset);
    return catalogItemAsset;
  }

  public static Order orderEntityTranslate(OrderEntity entity) {
    Order order = new Order();
    BeanUtils.copyProperties(entity, order);
    return order;
  }

  public static OrderItem orderItemEntityTranslate(OrderItemEntity entity) {
    OrderItem orderItem = new OrderItem();
    BeanUtils.copyProperties(entity, orderItem);
    return orderItem;
  }
}
