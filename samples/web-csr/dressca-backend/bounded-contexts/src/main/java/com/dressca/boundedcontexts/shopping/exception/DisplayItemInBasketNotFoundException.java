package com.dressca.boundedcontexts.shopping.exception;

import com.dressca.systemcommon.exception.LogicException;
import com.dressca.boundedcontexts.shopping.constant.ShoppingExceptionIdConstants;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * 買い物かご内に想定する商品が存在しないことを表す例外クラスです。
 */
public class DisplayItemInBasketNotFoundException extends LogicException {

  /**
   * 買い物かご ID と存在しなかった陳列品の陳列品 ID のリストを指定して、
   * {@link DisplayItemInBasketNotFoundException} クラスの新しいインスタンスを初期化します。
   *
   * @param basketId 買い物かご ID 。
   * @param displayItemIds 陳列品 ID のリスト。
   */
  public DisplayItemInBasketNotFoundException(UUID basketId, List<UUID> displayItemIds) {
    super(null, ShoppingExceptionIdConstants.E_DISPLAY_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] {String.valueOf(basketId), convertDisplayItemIds(displayItemIds)},
        new String[] {String.valueOf(basketId), convertDisplayItemIds(displayItemIds)});
  }

  /**
   * 陳列品 ID のリストを文字列に変換します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 文字列に変換された陳列品 ID のリスト。
   */
  private static String convertDisplayItemIds(List<UUID> displayItemIds) {
    StringJoiner sj = new StringJoiner(",");
    displayItemIds.stream().forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
