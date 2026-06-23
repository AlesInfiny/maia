package com.dressca.applicationcore.displayitem;

import java.util.List;

/**
 * 掲載ブランドのリポジトリのインターフェースです。
 */
public interface DisplayBrandRepository {
  /**
   * すべての掲載ブランドを取得します。
   * 
   * @return 掲載ブランドのリスト。
   */
  List<DisplayBrand> getAll();
}
