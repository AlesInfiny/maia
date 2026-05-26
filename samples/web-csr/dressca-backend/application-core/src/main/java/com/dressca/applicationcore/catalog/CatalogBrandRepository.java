package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.UUID;

/**
 * カタログブランドのリポジトリのインターフェースです。
 */
public interface CatalogBrandRepository {

  /**
   * すべてのカタログブランドを取得します。
   *
   * @return カタログブランドのリスト。
   */
  List<CatalogBrand> getAll();

  /**
   * 指定した ID のカタログブランドを取得します。
   *
   * @return 条件に一致するカタログブランド。
   */
  CatalogBrand findById(UUID id);
}
