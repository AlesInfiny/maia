package com.dressca.applicationcore.displayitem;

import java.util.Arrays;
import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 掲載品が存在しないことを表す例外です。
 */
public class DisplayItemNotFoundException extends LogicException {

  /**
   * 見つからなかった掲載品 ID を指定して {@link DisplayItemNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param displayItemId 見つからなかった掲載品 ID 。
   */
  public DisplayItemNotFoundException(long displayItemId) {
    super(null, ExceptionIdConstants.E_DISPLAY_ITEM_ID_NOT_FOUND,
        new String[] {String.valueOf(displayItemId)}, new String[] {String.valueOf(displayItemId)});
  }

  /**
   * 見つからなかった複数の掲載品 ID を指定して {@link DisplayItemNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param displayItemIds 見つからなかった複数の掲載品 ID 。
   * 
   */
  public DisplayItemNotFoundException(long... displayItemIds) {
    super(null, ExceptionIdConstants.E_DISPLAY_ITEM_ID_NOT_FOUND,
        new String[] {joinDisplayItemIds(displayItemIds)},
        new String[] {joinDisplayItemIds(displayItemIds)});
  }

  /**
   * 複数の掲載品 ID をカンマ区切りの文字列に変換します。
   * 
   * @param displayItemIds 掲載品 ID の配列。
   * @return 掲載品 ID をカンマ区切りの文字列に変換した結果。
   */
  private static String joinDisplayItemIds(long... displayItemIds) {
    return String.join(", ",
        Arrays.stream(displayItemIds).mapToObj(String::valueOf).toArray(String[]::new));
  }
}
