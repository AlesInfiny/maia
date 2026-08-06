package com.dressca.applicationcore.displayitem;

import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 陳列品が存在しないことを表す例外です。
 */
public class DisplayItemNotFoundException extends LogicException {

  /**
   * 見つからなかった陳列品 ID を指定して {@link DisplayItemNotFoundException} クラスのインスタンスを初期化します。
   *
   * @param displayItemId 見つからなかった陳列品 ID 。
   */
  public DisplayItemNotFoundException(UUID displayItemId) {
    super(null, ExceptionIdConstants.E_DISPLAY_ID_NOT_FOUND,
        new String[] {String.valueOf(displayItemId)}, new String[] {String.valueOf(displayItemId)});
  }

  /**
   * 見つからなかった複数の陳列品 ID を指定して {@link DisplayItemNotFoundException} クラスのインスタンスを初期化します。
   *
   * @param displayItemIds 見つからなかった複数の陳列品 ID 。
   */
  public DisplayItemNotFoundException(UUID... displayItemIds) {
    super(null, ExceptionIdConstants.E_DISPLAY_ID_NOT_FOUND,
        new String[] {joinDisplayItemIds(displayItemIds)},
        new String[] {joinDisplayItemIds(displayItemIds)});
  }

  /**
   * 複数の陳列品 ID をカンマ区切りの文字列に変換します。
   *
   * @param displayItemIds 陳列品 ID の配列。
   * @return 陳列品 ID をカンマ区切りの文字列に変換した結果。
   */
  private static String joinDisplayItemIds(UUID... displayItemIds) {
    return String.join(", ",
        Arrays.stream(displayItemIds).map(String::valueOf).toArray(String[]::new));
  }
}
