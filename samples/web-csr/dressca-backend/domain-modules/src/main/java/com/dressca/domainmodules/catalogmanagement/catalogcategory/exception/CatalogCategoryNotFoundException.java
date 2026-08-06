package com.dressca.domainmodules.catalogmanagement.catalogcategory.exception;

import com.dressca.domainmodules.common.constant.ExceptionIdConstants;
import com.dressca.domainmodules.common.exception.LogicException;
import java.util.UUID;

/**
 * カタログカテゴリが存在しないことを表す例外です。
 */
public class CatalogCategoryNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログカテゴリ ID を指定して、 {@link CatalogItemNotExistingInRepositoryException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogCategoryId 見つからなかったカタログカテゴリ ID 。
   */
  public CatalogCategoryNotFoundException(UUID catalogCategoryId) {
    super(null, ExceptionIdConstants.E_CATALOG_CATEGORY_NOT_FOUND,
        new String[] {String.valueOf(catalogCategoryId)},
        new String[] {String.valueOf(catalogCategoryId)});
  }
}
