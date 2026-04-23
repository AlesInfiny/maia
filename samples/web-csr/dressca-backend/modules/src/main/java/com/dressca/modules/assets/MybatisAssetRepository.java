package com.dressca.modules.assets;

import java.util.Optional;
import com.dressca.modules.assets.entity.AssetEntityExample;
import com.dressca.modules.assets.mapper.AssetMapper;
import com.dressca.modules.common.translator.EntityTranslator;
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
