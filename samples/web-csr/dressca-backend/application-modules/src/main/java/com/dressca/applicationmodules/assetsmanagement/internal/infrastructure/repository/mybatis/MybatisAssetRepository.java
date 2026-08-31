package com.dressca.applicationmodules.assetsmanagement.internal.infrastructure.repository.mybatis;

import com.dressca.applicationmodules.assetsmanagement.entity.Asset;
import com.dressca.applicationmodules.assetsmanagement.internal.domain.repository.AssetRepository;
import com.dressca.applicationmodules.assetsmanagement.internal.infrastructure.repository.mybatis.generated.entity.AssetEntityExample;
import com.dressca.applicationmodules.assetsmanagement.internal.infrastructure.repository.mybatis.generated.mapper.AssetMapper;
import com.dressca.applicationmodules.assetsmanagement.internal.infrastructure.repository.mybatis.translator.AssetEntityTranslator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * アセットのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisAssetRepository implements AssetRepository {

  private final AssetMapper assetMapper;

  @Override
  public Optional<Asset> findByAssetCode(String assetCode) {
    AssetEntityExample example = new AssetEntityExample();
    example.createCriteria().andAssetCodeEqualTo(assetCode);

    return assetMapper.selectByExample(example).stream()
        .map(AssetEntityTranslator::assetEntityTranslate).findFirst();
  }
}
