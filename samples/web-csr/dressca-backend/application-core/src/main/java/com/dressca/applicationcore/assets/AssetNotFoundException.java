package com.dressca.applicationcore.assets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

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
