package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 商品が存在しないことを表す例外です。
 */
public class CatalogNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログ ID を指定して例外を作成します。
   * 
   * @param catalogId 見つからなかったカタログ ID 。
   */
  public CatalogNotFoundException(long catalogId) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND, new String[] { String.valueOf(catalogId) },
        new String[] { String.valueOf(catalogId) });
  }

  /**
   * カタログに商品が存在しないことを表す例外を作成します。
   */
  public CatalogNotFoundException() {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND, null, null);
  }
}