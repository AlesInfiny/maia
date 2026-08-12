package com.dressca.boundedcontexts.assetsmanagement.exception;

import com.dressca.boundedcontexts.assetsmanagement.constant.AssetManagementExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * アセットが存在しないことを表す例外クラスです。
 */
public class AssetNotFoundException extends LogicException {

  /**
   * 見つからなかったアセットコードを指定して、 {@link AssetNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param assetCode 見つからなかったアセットコード。
   */
  public AssetNotFoundException(String assetCode) {
    super(null, AssetManagementExceptionIdConstants.E_ASSET_NOT_FOUND, new String[] {assetCode},
        new String[] {assetCode});
  }
}
