package com.dressca.applicationcore.catalog;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * カタログブランドが存在しないことを表す例外です。
 */
public class CatalogBrandNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログブランド Id を指定して
   * {@link CatalogItemNotExistingInRepositoryException} クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogBrandId 見つからなかったカタログブランド Id 。
   */
  public CatalogBrandNotFoundException(long catalogBrandId) {
    super(null, ExceptionIdConstant.E_CATALOG0002, new String[] { String.valueOf(
        catalogBrandId) },
        new String[] { String.valueOf(catalogBrandId) });
  }

}