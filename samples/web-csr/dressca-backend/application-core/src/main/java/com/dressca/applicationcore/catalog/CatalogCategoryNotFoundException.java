package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * カタログカテゴリが存在しないことを表す例外です。
 */
public class CatalogCategoryNotFoundException extends LogicException {

  public CatalogCategoryNotFoundException(UUID catalogCategoryId) {
    super(null, ExceptionIdConstants.E_CATALOG_CATEGORY_NOT_FOUND,
        new String[] {String.valueOf(catalogCategoryId)},
        new String[] {String.valueOf(catalogCategoryId)});
  }
}
