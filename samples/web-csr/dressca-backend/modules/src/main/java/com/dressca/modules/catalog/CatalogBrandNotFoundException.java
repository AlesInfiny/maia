package com.dressca.modules.catalog;

import com.dressca.modules.common.exception.LogicException;
import com.dressca.modules.constants.ExceptionIdConstants;

/**
 * カタログブランドが存在しないことを表す例外クラスです。
 */
public class CatalogBrandNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログブランド ID を指定して、 {@link CatalogItemNotExistingInRepositoryException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogBrandId 見つからなかったカタログブランド ID 。
   */
  public CatalogBrandNotFoundException(long catalogBrandId) {
    super(null, ExceptionIdConstants.E_CATALOG_BRAND_NOT_FOUND,
        new String[] {String.valueOf(catalogBrandId)},
        new String[] {String.valueOf(catalogBrandId)});
  }
}
