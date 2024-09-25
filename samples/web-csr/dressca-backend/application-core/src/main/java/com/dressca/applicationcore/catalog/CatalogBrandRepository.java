package com.dressca.applicationcore.catalog;

import java.util.List;

import java.util.Optional;;

/**
 * カタログブランドリポジトリ。
 */
public interface CatalogBrandRepository {
  /**
   * すべてのカタログブランドを取得します。
   * 
   * @return カタログブランドのリスト
   */
  List<CatalogBrand> getAll();

  /**
   * 指定した ID のカタログブランドを取得します。
   * 
   * @return 条件に一致するカタログブランド。存在しない場合、空のOptional.
   */
  Optional<CatalogBrand> findById(long id);
}
