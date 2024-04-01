package com.dressca.applicationcore.baskets;

import java.util.List;
import java.util.StringJoiner;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かご内に想定する商品が存在しないことを表す例外です。
 */
public class CatalogItemInBasketNotFoundException extends LogicException {
  public CatalogItemInBasketNotFoundException(List<Long> catalogIds, long basketId) {
    super(null, ExceptionIdConstant.E_BASKET0002,
        new String[] { String.valueOf(basketId), convertCatalogIds(catalogIds) },
        new String[] { String.valueOf(basketId), convertCatalogIds(catalogIds) });
  }

  static private String convertCatalogIds(List<Long> catalogIds) {
    StringJoiner sj = new StringJoiner(",");
    catalogIds.stream().forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
