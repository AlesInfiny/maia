package com.dressca.applicationcore.displayitem;

import java.util.List;
import java.util.UUID;

/**
 * 陳列カテゴリのリポジトリのインターフェースです。
 */
public interface DisplayCategoryRepository {

  /**
   * すべての陳列カテゴリを取得します。
   *
   * @return 陳列カテゴリのリスト。
   */
  List<DisplayCategory> getAll();

  /**
   * 指定した ID の陳列カテゴリを取得します。
   *
   * @return 条件に一致する陳列カテゴリ。
   */
  DisplayCategory findById(UUID id);
}
