package com.dressca.applicationcore.catalog;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 商品が存在しないことを表す例外です。
 */
public class CatalogNotFoundException extends LogicException {

  public CatalogNotFoundException(UUID catalogId) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND,
        new String[] {String.valueOf(catalogId)}, new String[] {String.valueOf(catalogId)});
  }

  public CatalogNotFoundException(UUID... catalogIds) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND,
        new String[] {joinCatalogIds(catalogIds)}, new String[] {joinCatalogIds(catalogIds)});
  }

  private static String joinCatalogIds(UUID... catalogIds) {
    return String.join(", ", Arrays.stream(catalogIds).map(String::valueOf).toArray(String[]::new));
  }
}
