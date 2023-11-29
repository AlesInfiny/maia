package com.dressca.applicationcore.catalog;

import java.util.List;

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
}
