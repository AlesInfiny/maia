package com.dressca.applicationcore.baskets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * 買い物かごに商品が存在しないことを表す例外です。
 */
public class CatalogItemInBasketNotFoundException extends LogicException {

  public CatalogItemInBasketNotFoundException(List<UUID> catalogIds, UUID basketId) {
    super(null, ExceptionIdConstants.E_CATALOG_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] {convertCatalogIds(catalogIds), String.valueOf(basketId)},
        new String[] {convertCatalogIds(catalogIds), String.valueOf(basketId)});
  }

  private static String convertCatalogIds(List<UUID> catalogIds) {
    StringJoiner sj = new StringJoiner(",");
    catalogIds.stream().forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
