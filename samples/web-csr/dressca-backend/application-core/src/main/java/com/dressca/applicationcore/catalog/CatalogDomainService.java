package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * カタログに関するドメインサービスを提供します。
 */
@Service
@AllArgsConstructor
public class CatalogDomainService {
  private CatalogRepository catalogRepository;
  private CatalogBrandRepository brandRepository;
  private CatalogCategoryRepository categoryRepository;

  /**
   * 指定したカタログアイテム Id のうち、存在するカタログアイテムの一覧を返却します。
   * 
   * @param catalogItemIds カタログアイテム Id のリスト
   * @return 存在するカタログアイテムの一覧
   */
  public List<CatalogItem> getExistCatalogItems(List<Long> catalogItemIds) {
    return this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
  }

  /**
   * 指定したカタログアイテム Id がリポジトリ内にすべて存在するかを取得します。
   * 
   * @param catalogItemIds カタログアイテム Id のリスト
   * @return すべて存在する場合は true、一部でも不在の場合は false。
   */
  public boolean existAll(List<Long> catalogItemIds) {
    List<CatalogItem> items = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<Long> notExistCatalogItemIds = catalogItemIds.stream()
        .filter(catalogItemId -> !this.existCatalogItemIdInItems(items, catalogItemId))
        .collect(Collectors.toList());

    return notExistCatalogItemIds.isEmpty();
  }

  /**
   * 指定した Id のカタログブランドがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogBrandId カタログブランド Id。
   * @return 指定したカタログブランドがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogBrand(long catalogBrandId) {
    return this.brandRepository.findById(catalogBrandId) != null;
  }

  /**
   * 指定した Id のカタログカテゴリがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogCategoryId カタログカテゴリ Id。
   * @return 指定したカタログカテゴリがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogCategory(long catalogCategoryId) {
    return this.categoryRepository.findById(catalogCategoryId) != null;
  }

  /**
   * 指定した Id のカタログアイテムがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogItemId カタログアイテム Id。
   * @return 指定したカタログアイテムがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogItem(long catalogItemId) {
    return this.catalogRepository.findById(catalogItemId) != null;
  }

  private boolean existCatalogItemIdInItems(List<CatalogItem> items, long catalogItemId) {
    return items.stream()
        .anyMatch(catalogItem -> catalogItem.getId() == catalogItemId);
  }
}
