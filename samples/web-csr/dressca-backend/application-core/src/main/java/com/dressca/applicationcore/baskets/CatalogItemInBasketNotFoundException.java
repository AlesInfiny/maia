package com.dressca.applicationcore.baskets;

import java.util.List;
import java.util.StringJoiner;
import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かご内に想定する商品が存在しないことを表す例外クラスです。
 */
public class CatalogItemInBasketNotFoundException extends LogicException {
  /**
   * 存在しなかった商品のカタログアイテム ID のリストと買い物かご ID を指定して、 {@link CatalogItemInBasketNotFoundException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogIds カタログアイテム ID のリスト。
   * @param basketId 買い物かご ID 。
   */
  public CatalogItemInBasketNotFoundException(List<Long> catalogIds, long basketId) {
    super(null, ExceptionIdConstants.E_CATALOG_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] {String.valueOf(basketId), convertCatalogIds(catalogIds)},
        new String[] {String.valueOf(basketId), convertCatalogIds(catalogIds)});
  }

  /**
   * カタログアイテム ID を文字列に変換します。
   * 
   * @param catalogIds カタログアイテム ID のリスト。
   * @return 文字列に変換されたカタログアイテム ID のリスト。
   */
  private static String convertCatalogIds(List<Long> catalogIds) {
    StringJoiner sj = new StringJoiner(",");
    catalogIds.stream()
        .forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
