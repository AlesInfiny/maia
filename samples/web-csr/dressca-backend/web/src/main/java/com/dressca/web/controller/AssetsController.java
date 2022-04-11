package com.dressca.web.controller;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetApplicationService;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.applicationcore.assets.AssetResourceInfo;
import com.dressca.applicationcore.assets.AssetTypes;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetsController {

  @Autowired
  private AssetApplicationService service;

  /**
   * アセットを取得します。
   * 
   * @param assetCode アセットコード
   * @return アセット
   */
  @GetMapping("{assetCode}")
  public ResponseEntity<Resource> get(@PathVariable String assetCode) throws LogicException {

    try {
      AssetResourceInfo assetResourceInfo = this.service.getAssetResourceInfo(assetCode);
      MediaType contentType = getContetType(assetResourceInfo.getAsset());

      return ResponseEntity.ok()
        .contentType(contentType)
        .body(assetResourceInfo.getResource());
    } catch (AssetNotFoundException e) {
      throw new LogicException(e, ExceptionIdConstant.E_ASSET0001, new String[] {assetCode}, new String[] {assetCode});
    } 
  }

  /**
   * アセットタイプから Content-Type に変換します。
   * @param asset アセット
   * @return Content-Type の名称
   */
  private MediaType getContetType(Asset asset) {
    switch(asset.getAssetType()) {
      case AssetTypes.png: 
        return MediaType.IMAGE_PNG;
      default:
        throw new IllegalArgumentException("指定したアセットのアセットタイプは Content-Type に変換できません。");
    }
  }
}
