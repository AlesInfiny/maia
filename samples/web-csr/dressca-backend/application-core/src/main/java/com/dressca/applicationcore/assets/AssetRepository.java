package com.dressca.applicationcore.assets;

import java.util.Optional;

/**
 * アセットのリポジトリのインターフェースです。
 */
public interface AssetRepository {

  /**
   * 指定したアセットコードの情報を取得します。 存在しない場合は 空の Optional を返します。
   * 
   * @param assetCode アセットコード。
   * @return アセット情報。
   */
  Optional<Asset> findByAssetCode(String assetCode);
}
