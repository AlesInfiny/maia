package com.dressca.applicationcore.display;

import java.util.List;
import java.util.UUID;

/**
 * 陳列ブランドのリポジトリのインターフェースです。
 */
public interface DisplayBrandRepository {

  /**
   * すべての陳列ブランドを取得します。
   *
   * @return 陳列ブランドのリスト。
   */
  List<DisplayBrand> getAll();

  /**
   * 指定した ID の陳列ブランドを取得します。
   *
   * @return 条件に一致する陳列ブランド。
   */
  DisplayBrand findById(UUID id);
}
