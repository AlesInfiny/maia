package com.dressca.applicationmodules.assetsmanagement;

import com.dressca.applicationmodules.assetsmanagement.constant.AssetManagementMessageIdConstants;
import com.dressca.applicationmodules.assetsmanagement.dto.AssetResourceInfo;
import com.dressca.applicationmodules.assetsmanagement.entity.Asset;
import com.dressca.applicationmodules.assetsmanagement.exception.AssetNotFoundException;
import com.dressca.applicationmodules.assetsmanagement.internal.domain.repository.AssetRepository;
import com.dressca.applicationmodules.assetsmanagement.internal.domain.store.AssetStore;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * アセット情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AssetApplicationService {

  private final AssetRepository repository;
  private final AssetStore store;
  private final MessageSource messages;
  private final AbstractStructuredLogger apLog;

  /**
   * 指定したアセットコードのアセット情報とリソースオブジェクトを取得します。
   * 
   * @param assetCode アセットコード。
   * @return アセット情報とそのリソースオブジェクト。
   * @throws AssetNotFoundException アセット情報が見つからなかった場合。
   */
  public AssetResourceInfo getAssetResourceInfo(String assetCode) throws AssetNotFoundException {

    apLog.debug(messages.getMessage(AssetManagementMessageIdConstants.D_ASSET_GET_ASSET,
        new Object[] {assetCode}, Locale.getDefault()));

    Asset asset = this.repository.findByAssetCode(assetCode)
        .orElseThrow(() -> new AssetNotFoundException(assetCode));
    Resource resource =
        this.store.getResource(asset).orElseThrow(() -> new AssetNotFoundException(assetCode));

    return new AssetResourceInfo(asset, resource);
  }
}
