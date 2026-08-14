package com.dressca.applicationmodules.catalogmanagement.internal.domain;

import com.dressca.applicationmodules.catalogmanagement.internal.domain.repository.CatalogBrandRepository;
import com.dressca.applicationmodules.catalogmanagement.internal.domain.repository.CatalogCategoryRepository;
import com.dressca.applicationmodules.catalogmanagement.internal.domain.repository.CatalogRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * カタログに関するドメインサービスです。
 */
@Service
@RequiredArgsConstructor
public class CatalogDomainService {
  private final CatalogRepository catalogRepository;
  private final CatalogBrandRepository brandRepository;
  private final CatalogCategoryRepository categoryRepository;

  /**
   * 指定した ID のカタログブランドがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogBrandId カタログブランド ID 。
   * @return 指定したカタログブランドがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogBrand(UUID catalogBrandId) {
    return this.brandRepository.findById(catalogBrandId) != null;
  }

  /**
   * 指定した ID のカタログカテゴリがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogCategoryId カタログカテゴリ ID 。
   * @return 指定したカタログカテゴリがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogCategory(UUID catalogCategoryId) {
    return this.categoryRepository.findById(catalogCategoryId) != null;
  }

  /**
   * 指定した ID のカタログアイテムがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @return 指定したカタログアイテムがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogItem(UUID catalogItemId) {
    return this.catalogRepository.findById(catalogItemId) != null;
  }
}
