package com.dressca.applicationcore.assets;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
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
    super(null, ExceptionIdConstant.E_ASSET0001, new String[] {assetCode}, new String[] {assetCode});
  }
}
