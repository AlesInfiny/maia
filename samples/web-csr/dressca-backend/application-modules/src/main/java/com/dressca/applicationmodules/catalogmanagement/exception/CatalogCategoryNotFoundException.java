package com.dressca.applicationmodules.catalogmanagement.exception;

import com.dressca.applicationmodules.catalogmanagement.internal.domain.constant.CatalogManagementExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * カタログカテゴリが存在しないことを表す例外です。
 */
public class CatalogCategoryNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログカテゴリ ID を指定して、 {@link CatalogCategoryNotFoundException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogCategoryId 見つからなかったカタログカテゴリ ID 。
   */
  public CatalogCategoryNotFoundException(UUID catalogCategoryId) {
    super(null, CatalogManagementExceptionIdConstants.E_CATALOG_CATEGORY_NOT_FOUND,
        new String[] {String.valueOf(catalogCategoryId)},
        new String[] {String.valueOf(catalogCategoryId)});
  }
}
