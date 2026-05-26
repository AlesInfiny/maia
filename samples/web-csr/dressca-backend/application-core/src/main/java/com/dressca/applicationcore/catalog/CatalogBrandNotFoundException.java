package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * カタログブランドが存在しないことを表す例外です。
 */
public class CatalogBrandNotFoundException extends LogicException {

  public CatalogBrandNotFoundException(UUID catalogBrandId) {
    super(null, ExceptionIdConstants.E_CATALOG_BRAND_NOT_FOUND,
        new String[] {String.valueOf(catalogBrandId)},
        new String[] {String.valueOf(catalogBrandId)});
  }
}
