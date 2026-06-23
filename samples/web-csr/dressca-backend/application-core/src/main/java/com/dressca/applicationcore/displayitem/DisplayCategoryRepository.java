package com.dressca.applicationcore.displayitem;

import java.util.List;

/**
 * 掲載カテゴリのリポジトリのインターフェースです。
 */
public interface DisplayCategoryRepository {

  /**
   * すべての掲載カテゴリを取得します。
   * 
   * @return 掲載カテゴリのリスト。
   */
  List<DisplayCategory> getAll();
}
