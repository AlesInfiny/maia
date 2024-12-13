package com.dressca.applicationcore.baskets;

import java.util.List;
import java.util.StringJoiner;
import com.dressca.applicationcore.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かご内に想定する商品が存在しないことを表す例外です。
 */
public class CatalogItemInBasketNotFoundException extends LogicException {
  /**
   * 買い物かご内に想定する商品がなかった場合、{@link CatalogItemInBasketNotFoundException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogIds カタログIDのリスト
   * @param basketId   買い物かごID
   */
  public CatalogItemInBasketNotFoundException(List<Long> catalogIds, long basketId) {
    super(null, ExceptionIdConstant.E_CATALOG_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] { String.valueOf(basketId), convertCatalogIds(catalogIds) },
        new String[] { String.valueOf(basketId), convertCatalogIds(catalogIds) });
  }

  /**
   * カタログIDを文字列に変換します。
   * 
   * @param catalogIds カタログIDのリスト
   * @return 文字列に変換されたカタログIDのリスト
   */
  private static String convertCatalogIds(List<Long> catalogIds) {
    StringJoiner sj = new StringJoiner(",");
    catalogIds.stream().forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
