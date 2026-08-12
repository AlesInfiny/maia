package com.dressca.boundedcontexts.catalogmanagement.exception;

import com.dressca.boundedcontexts.catalogmanagement.constant.CatalogManagementExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.UUID;

/**
 * カタログブランドが存在しないことを表す例外クラスです。
 */
public class CatalogBrandNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログブランド ID を指定して、 {@link CatalogBrandNotFoundException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogBrandId 見つからなかったカタログブランド ID 。
   */
  public CatalogBrandNotFoundException(UUID catalogBrandId) {
    super(null, CatalogManagementExceptionIdConstants.E_CATALOG_BRAND_NOT_FOUND,
        new String[] {String.valueOf(catalogBrandId)},
        new String[] {String.valueOf(catalogBrandId)});
  }
}
