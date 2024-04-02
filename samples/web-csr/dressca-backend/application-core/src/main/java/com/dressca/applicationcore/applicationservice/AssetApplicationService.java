package com.dressca.applicationcore.applicationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.applicationcore.assets.AssetRepository;
import com.dressca.applicationcore.assets.AssetResourceInfo;
import com.dressca.applicationcore.assets.AssetStore;
import lombok.AllArgsConstructor;

/**
 * アセット情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AssetApplicationService {

  @Autowired
  private AssetRepository repository;
  @Autowired
  private AssetStore store;

  /**
   * 指定したアセットコードのアセット情報とリソースオブジェクトを取得します。
   * 
   * @param assetCode アセットコード
   * @return アセット情報とそのリソースオブジェクト
   * @throws AssetNotFoundException アセット情報が見つからなかった場合
   */
  public AssetResourceInfo getAssetResourceInfo(String assetCode) throws AssetNotFoundException {
    Asset asset = this.repository.findByAssetCode(assetCode)
        .orElseThrow(() -> new AssetNotFoundException(assetCode));
    Resource resource = this.store.getResource(asset)
        .orElseThrow(() -> new AssetNotFoundException(assetCode));

    return new AssetResourceInfo(asset, resource);
  }
}
