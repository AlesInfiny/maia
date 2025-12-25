package com.dressca.applicationcore.applicationservice;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.applicationcore.assets.AssetRepository;
import com.dressca.applicationcore.assets.AssetResourceInfo;
import com.dressca.applicationcore.assets.AssetStore;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
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
  @Autowired
  private MessageSource messages;
  @Autowired
  private AbstractStructuredLogger apLog;

  /**
   * 指定したアセットコードのアセット情報とリソースオブジェクトを取得します。
   * 
   * @param assetCode アセットコード。
   * @return アセット情報とそのリソースオブジェクト。
   * @throws AssetNotFoundException アセット情報が見つからなかった場合。
   */
  public AssetResourceInfo getAssetResourceInfo(String assetCode) throws AssetNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_ASSET_GET_ASSET, new Object[] {assetCode},
        Locale.getDefault()));

    Asset asset = this.repository.findByAssetCode(assetCode)
        .orElseThrow(() -> new AssetNotFoundException(assetCode));
    Resource resource = this.store.getResource(asset)
        .orElseThrow(() -> new AssetNotFoundException(assetCode));

    return new AssetResourceInfo(asset, resource);
  }
}
