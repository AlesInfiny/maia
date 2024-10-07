package com.dressca.applicationcore.catalog;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * カタログカテゴリが存在しないことを表す例外です。
 */
public class CatalogCategoryNotFoundException extends LogicException {

    public CatalogCategoryNotFoundException(long catalogCategoryId) {
        super(null, ExceptionIdConstant.E_CATALOG0003, new String[] { String.valueOf(
                catalogCategoryId) },
                new String[] { String.valueOf(catalogCategoryId) });
    }

}