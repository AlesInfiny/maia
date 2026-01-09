package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * カタログに関するドメインサービスです。
 */
@Service
@AllArgsConstructor
public class CatalogDomainService {
  private CatalogRepository catalogRepository;
  private CatalogBrandRepository brandRepository;
  private CatalogCategoryRepository categoryRepository;

  /**
   * 指定したカタログアイテム ID のうち、存在するカタログアイテムの一覧を取得します。
   * 
   * @param catalogItemIds カタログアイテム ID のリスト。
   * @return 存在するカタログアイテムの一覧。
   */
  public List<CatalogItem> getExistCatalogItems(List<Long> catalogItemIds) {
    return this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
  }

  /**
   * 指定したカタログアイテム ID がリポジトリ内にすべて存在するかを取得します。
   * 
   * @param catalogItemIds カタログアイテム ID のリスト。
   * @return すべて存在する場合は true 、一部でも不在の場合は false 。
   */
  public boolean existAll(List<Long> catalogItemIds) {
    List<CatalogItem> items = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<Long> notExistCatalogItemIds = catalogItemIds.stream()
        .filter(catalogItemId -> !this.existCatalogItemIdInItems(items, catalogItemId))
        .collect(Collectors.toList());

    return notExistCatalogItemIds.isEmpty();
  }

  /**
   * 指定した ID のカタログブランドがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogBrandId カタログブランド ID 。
   * @return 指定したカタログブランドがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogBrand(long catalogBrandId) {
    return this.brandRepository.findById(catalogBrandId) != null;
  }

  /**
   * 指定した ID のカタログカテゴリがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogCategoryId カタログカテゴリ ID 。
   * @return 指定したカタログカテゴリがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogCategory(long catalogCategoryId) {
    return this.categoryRepository.findById(catalogCategoryId) != null;
  }

  /**
   * 指定した ID のカタログアイテムがリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @return 指定したカタログアイテムがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogItem(long catalogItemId) {
    return this.catalogRepository.findById(catalogItemId) != null;
  }

  /**
   * 指定した ID のカタログアイテムが、削除済みカタログアイテムを含むリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @return 指定したカタログアイテムがリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existCatalogItemIncludingDeleted(long catalogItemId) {
    return this.catalogRepository.findByIdIncludingDeleted(catalogItemId) != null;
  }

  private boolean existCatalogItemIdInItems(List<CatalogItem> items, long catalogItemId) {
    return items.stream().anyMatch(catalogItem -> catalogItem.getId() == catalogItemId);
  }
}
