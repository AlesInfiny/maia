package com.dressca.infrastructure.repository.mybatis;

import java.util.Optional;
import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.AssetMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

/**
 * アセットのリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MybatisAssetRepository implements AssetRepository {

  @Autowired
  private AssetMapper assetMapper;

  @Override
  public Optional<Asset> findByAssetCode(String assetCode) {
    AssetEntityExample example = new AssetEntityExample();
    example.createCriteria().andAssetCodeEqualTo(assetCode);

    return assetMapper.selectByExample(example).stream().map(EntityTranslator::assetEntityTranslate)
        .findFirst();
  }
}
