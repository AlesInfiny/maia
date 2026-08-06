package com.dressca.domainmodules.catalogmanagement.internal.infrastructure.mybatis.translator;

import com.dressca.domainmodules.catalogmanagement.models.CatalogBrand;
import com.dressca.domainmodules.catalogmanagement.models.CatalogCategory;
import com.dressca.domainmodules.catalogmanagement.models.CatalogItem;
import com.dressca.domainmodules.catalogmanagement.models.CatalogItemAsset;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogItemAssetEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogItemEntity;
import org.springframework.beans.BeanUtils;

/**
 * カタログ管理の文脈において、テーブルエンティティとエンティティを相互に変換するクラスです。
 */
public class CatalogEntityTranslator {

  /**
   * テーブルエンティティ： {@link CatalogBrandEntity} をエンティティ：{@link CatalogBrand} に変換します。
   *
   * @param entity {@link CatalogBrandEntity} オブジェクト。
   * @return {@link CatalogBrand} オブジェクト。
   */
  public static CatalogBrand catalogBrandEntityTranslate(CatalogBrandEntity entity) {
    CatalogBrand catalogBrand = new CatalogBrand();
    BeanUtils.copyProperties(entity, catalogBrand);
    return catalogBrand;
  }

  /**
   * テーブルエンティティ： {@link CatalogCategoryEntity} をエンティティ： {@link CatalogCategory} に変換します。
   *
   * @param entity {@link CatalogCategoryEntity} オブジェクト。
   * @return {@link CatalogCategory} オブジェクト。
   */
  public static CatalogCategory catalogCategoryEntityTranslate(CatalogCategoryEntity entity) {
    CatalogCategory catalogCategory = new CatalogCategory();
    BeanUtils.copyProperties(entity, catalogCategory);
    return catalogCategory;
  }

  /**
   * テーブルエンティティ： {@link CatalogItemEntity} をエンティティ： {@link CatalogItem} に変換します。
   *
   * @param entity {@link CatalogItemEntity} オブジェクト。
   * @return {@link CatalogItem} オブジェクト。
   */
  public static CatalogItem catalogItemEntityTranslate(CatalogItemEntity entity) {
    CatalogItem catalogItem = new CatalogItem();
    BeanUtils.copyProperties(entity, catalogItem);
    catalogItem.setDeleted(entity.getIsDeleted());
    return catalogItem;
  }

  /**
   * エンティティ： {@link CatalogItem} からテーブルエンティティ： {@link CatalogItemEntity} に変換します。
   *
   * @param catalogItem {@link CatalogItem} オブジェクト。
   * @return {@link CatalogItemEntity} オブジェクト。
   */
  public static CatalogItemEntity createCatalogItemEntity(CatalogItem catalogItem) {
    CatalogItemEntity entity = new CatalogItemEntity();
    BeanUtils.copyProperties(catalogItem, entity);
    entity.setIsDeleted(catalogItem.isDeleted());
    return entity;
  }

  /**
   * テーブルエンティティ： {@link CatalogItemAssetEntity} をエンティティ： {@link CatalogItemAsset} に変換します。
   *
   * @param entity {@link CatalogItemAssetEntity} オブジェクト。
   * @return {@link CatalogItemAsset} オブジェクト。
   */
  public static CatalogItemAsset catalogItemAssetEntityTranslate(CatalogItemAssetEntity entity) {
    CatalogItemAsset catalogItemAsset = new CatalogItemAsset();
    BeanUtils.copyProperties(entity, catalogItemAsset);
    return catalogItemAsset;
  }
}
