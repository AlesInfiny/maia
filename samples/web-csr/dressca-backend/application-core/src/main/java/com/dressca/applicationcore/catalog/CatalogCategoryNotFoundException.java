package com.dressca.applicationcore.catalog;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * カタログカテゴリが存在しないことを表す例外です。
 */
public class CatalogCategoryNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログカテゴリ Id を指定して
   * {@link CatalogItemNotExistingInRepositoryException} クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogCategoryId 見つからなかったカタログカテゴリ Id 。
   */
  public CatalogCategoryNotFoundException(long catalogCategoryId) {
    super(null, ExceptionIdConstant.E_CATALOG0003, new String[] { String.valueOf(
        catalogCategoryId) },
        new String[] { String.valueOf(catalogCategoryId) });
  }

}