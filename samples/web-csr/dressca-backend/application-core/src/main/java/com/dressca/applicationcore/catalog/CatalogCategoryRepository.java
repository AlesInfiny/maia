package com.dressca.applicationcore.catalog;

import java.util.List;

/**
 * カタログカテゴリリポジトリ。
 */
public interface CatalogCategoryRepository {
  /**
   * すべてのカタログカテゴリを取得します。
   * @return カタログカテゴリのリスト
   */
  List<CatalogCategory> getAll();
}
