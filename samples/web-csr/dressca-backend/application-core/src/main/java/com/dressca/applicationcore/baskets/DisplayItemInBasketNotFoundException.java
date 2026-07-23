package com.dressca.applicationcore.baskets;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * 買い物かご内に想定する商品が存在しないことを表す例外クラスです。
 */
public class DisplayItemInBasketNotFoundException extends LogicException {

  /**
   * 存在しなかった商品の陳列品 ID のリストと買い物かご ID を指定して、
   * {@link DisplayItemInBasketNotFoundException} クラスの新しいインスタンスを初期化します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @param basketId 買い物かご ID 。
   */
  public DisplayItemInBasketNotFoundException(List<UUID> displayItemIds, UUID basketId) {
    super(null, ExceptionIdConstants.E_DISPLAY_ITEM_ID_DOES_NOT_EXIST_IN_BASKET,
        new String[] {convertDisplayItemIds(displayItemIds), String.valueOf(basketId)},
        new String[] {convertDisplayItemIds(displayItemIds), String.valueOf(basketId)});
  }

  /**
   * 陳列品 ID を文字列に変換します。
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
