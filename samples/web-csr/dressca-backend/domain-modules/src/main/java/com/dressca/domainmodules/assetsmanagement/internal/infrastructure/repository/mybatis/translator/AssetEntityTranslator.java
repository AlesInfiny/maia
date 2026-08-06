package com.dressca.domainmodules.assetsmanagement.internal.infrastructure.repository.mybatis.translator;

import com.dressca.domainmodules.assetsmanagement.internal.domain.Asset;
import com.dressca.domainmodules.common.mybatis.generated.entity.AssetEntity;
import org.springframework.beans.BeanUtils;

/**
 * 静的アセット管理の文脈において、テーブルエンティティとエンティティを相互に変換するクラスです。
 */
public class AssetEntityTranslator {

  /**
   * テーブルエンティティ： {@link AssetEntity} をエンティティ： {@link Asset} に変換します。
   *
   * @param entity {@link AssetEntity} オブジェクト。
   * @return {@link Asset} オブジェクト。
   */
  public static Asset assetEntityTranslate(AssetEntity entity) {
    Asset asset = new Asset();
    BeanUtils.copyProperties(entity, asset);
    return asset;
  }
}
