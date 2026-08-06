package com.dressca.domainmodules.catalogmanagement.internal.domain.repository;

import java.util.List;
import java.util.UUID;
import com.dressca.domainmodules.catalogmanagement.models.CatalogCategory;

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
