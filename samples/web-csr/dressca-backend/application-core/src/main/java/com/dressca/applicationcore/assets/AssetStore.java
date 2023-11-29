package com.dressca.applicationcore.assets;

import java.util.Optional;
import org.springframework.core.io.Resource;

/**
 * アセットのストア。
 */
public interface AssetStore {

  /**
   * 指定したアセット情報のリソースオブジェクトを取得します。
   * 見つからない場合は 空のOptional を返します。
   * 
   * @param asset アセット情報。
   * @return リソースオブジェクト。見つからない場合は 空のOptional。
   */
  Optional<Resource> getResource(Asset asset);
}
