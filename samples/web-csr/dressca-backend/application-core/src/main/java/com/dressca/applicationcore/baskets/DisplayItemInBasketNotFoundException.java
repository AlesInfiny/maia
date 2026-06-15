package com.dressca.applicationcore.baskets;

import java.util.List;
import java.util.StringJoiner;
import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 買い物かご内に想定する掲載品が存在しないことを表す例外クラスです。
 */
public class DisplayItemInBasketNotFoundException extends LogicException {
  /**
   * 存在しなかった掲載品 ID のリストと買い物かご ID を指定して、 {@link DisplayItemInBasketNotFoundException}
   * クラスの新しいインスタンスを初期化します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @param basketId 買い物かご ID 。
   */
  public DisplayItemInBasketNotFoundException(List<Long> displayItemIds, long basketId) {
    super(null, ExceptionIdConstants.E_DISPLAY_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] {String.valueOf(basketId), convertDisplayItemIds(displayItemIds)},
        new String[] {String.valueOf(basketId), convertDisplayItemIds(displayItemIds)});
  }

  /**
   * 掲載品 ID を文字列に変換します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 文字列に変換された掲載品 ID のリスト。
   */
  private static String convertDisplayItemIds(List<Long> displayItemIds) {
    StringJoiner sj = new StringJoiner(",");
    displayItemIds.stream().forEach(id -> sj.add(String.valueOf(id)));
    return sj.toString();
  }
}
