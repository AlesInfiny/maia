package com.dressca.domainmodules.assetsmanagement.internal.domain.repository;

import java.util.Optional;
import com.dressca.domainmodules.assetsmanagement.internal.domain.Asset;

/**
 * アセットのリポジトリのインターフェースです。
 */
public interface AssetRepository {

  /**
   * 指定したアセットコードの情報を取得します。存在しない場合は空の Optional を返します。
   * 
   * @param assetCode アセットコード。
   * @return アセット情報。
   */
  Optional<Asset> findByAssetCode(String assetCode);
}
