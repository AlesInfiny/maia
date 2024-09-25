package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.Optional;

/**
 * カタログカテゴリリポジトリ。
 */
public interface CatalogCategoryRepository {
  /**
   * すべてのカタログカテゴリを取得します。
   * 
   * @return カタログカテゴリのリスト
   */
  List<CatalogCategory> getAll();

  /**
   * 指定した ID のカタログカテゴリを取得します。
   * 
   * @return 条件に一致するカタログカテゴリ。存在しない場合、空のOptional.
   */
  Optional<CatalogCategory> findById(long id);
}
