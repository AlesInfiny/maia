package com.dressca.applicationcore.assets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * アセット情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
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
