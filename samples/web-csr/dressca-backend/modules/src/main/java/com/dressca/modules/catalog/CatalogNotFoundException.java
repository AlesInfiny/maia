package com.dressca.modules.catalog;

import com.dressca.modules.common.exception.LogicException;
import com.dressca.modules.constants.ExceptionIdConstants;

/**
 * 商品が存在しないことを表す例外です。
 */
public class CatalogNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログ ID を指定して {@link CatalogNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param catalogId 見つからなかったカタログ ID 。
   */
  public CatalogNotFoundException(long catalogId) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND,
        new String[] {String.valueOf(catalogId)}, new String[] {String.valueOf(catalogId)});
  }

  /**
   * {@link CatalogNotFoundException} クラスのインスタンスを初期化します。
   */
  public CatalogNotFoundException() {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND, null, null);
  }
}
