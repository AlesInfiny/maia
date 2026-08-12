package com.dressca.boundedcontexts.shopping.internal.domain.repository;

import com.dressca.boundedcontexts.shopping.model.DisplayItemCategory;
import java.util.List;
import java.util.UUID;

/**
 * 陳列品カテゴリのリポジトリのインターフェースです。
 */
public interface DisplayItemCategoryRepository {

  /**
   * すべての陳列品カテゴリを取得します。
   *
   * @return 陳列品カテゴリのリスト。
   */
  List<DisplayItemCategory> getAll();

  /**
   * 指定した ID の陳列品カテゴリを取得します。
   * 
   * @param id 陳列品カテゴリの ID 。
   * @return 条件に一致する陳列品カテゴリ。
   */
  DisplayItemCategory findById(UUID id);
}
