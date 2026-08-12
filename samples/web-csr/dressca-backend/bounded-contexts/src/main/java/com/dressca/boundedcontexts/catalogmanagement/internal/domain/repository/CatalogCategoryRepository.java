package com.dressca.boundedcontexts.catalogmanagement.internal.domain.repository;

import com.dressca.boundedcontexts.catalogmanagement.model.CatalogCategory;
import java.util.List;
import java.util.UUID;

/**
 * カタログカテゴリのリポジトリのインターフェースです。
 */
public interface CatalogCategoryRepository {

  /**
   * すべてのカタログカテゴリを取得します。
   *
   * @return カタログカテゴリのリスト。
   */
  List<CatalogCategory> getAll();

  /**
   * 指定した ID のカタログカテゴリを取得します。
   *
   * @return 条件に一致するカタログカテゴリ。
   */
  CatalogCategory findById(UUID id);
}
