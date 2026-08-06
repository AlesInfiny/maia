package com.dressca.domainmodules.assetsmanagement.internal.domain.exception;

import com.dressca.domainmodules.common.constant.ExceptionIdConstants;
import com.dressca.domainmodules.common.exception.LogicException;

/**
 * アセットが存在しないことを表す例外クラスです。
 */
public class AssetNotFoundException extends LogicException {

  /**
   * 見つからなかったアセットコードを指定して、 {@link AssetNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param assetCode 見つからなかった買い物かご ID 。
   */
  public AssetNotFoundException(String assetCode) {
    super(null, ExceptionIdConstants.E_ASSET_NOT_FOUND, new String[] {assetCode},
        new String[] {assetCode});
  }
}
