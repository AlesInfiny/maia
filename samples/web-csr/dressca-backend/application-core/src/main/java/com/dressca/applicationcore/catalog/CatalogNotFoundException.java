package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 商品が存在しないことを表す例外です。
 */
public class CatalogNotFoundException extends LogicException {
  public CatalogNotFoundException(long catalogId) {
    super(null, ExceptionIdConstant.E_CATALOG_ID_NOT_FOUND, new String[] { String.valueOf(catalogId) },
        new String[] { String.valueOf(catalogId) });
  }

  public CatalogNotFoundException() {
    super(null, ExceptionIdConstant.E_CATALOG_ID_NOT_FOUND, null, null);
  }
}