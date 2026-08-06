package com.dressca.domainmodules.shopping.internal.domain.repository;

import java.util.List;
import java.util.UUID;
import com.dressca.domainmodules.shopping.models.DisplayItemBrand;

/**
 * 陳列品ブランドのリポジトリのインターフェースです。
 */
public interface DisplayItemBrandRepository {

  /**
   * すべての陳列品ブランドを取得します。
   *
   * @return 陳列品ブランドのリスト。
   */
  List<DisplayItemBrand> getAll();

  /**
   * 指定した ID の陳列品ブランドを取得します。
   * 
   * @param id 陳列品ブランドの ID 。
   * @return 条件に一致する陳列品ブランド。
   */
  DisplayItemBrand findById(UUID id);
}
