package com.dressca.applicationcore.assets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * アセットが存在しないことを表す例外クラスです。
 */
public class AssetNotFoundException extends LogicException {

  /**
   * 見つからなかったアセットコードを指定して例外を作成します。
   * 
   * @param assetCode 見つからなかった買い物かご Id
   */
  public AssetNotFoundException(String assetCode) {
    super(null, ExceptionIdConstants.E_ASSET_NOT_FOUND, new String[] { assetCode },
        new String[] { assetCode });
  }
}
