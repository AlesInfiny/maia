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

  /**
   * 指定したカタログアイテム Id のうち、存在するカタログアイテムの一覧を返却します。
   * @param catalogItemIds カタログアイテム Id のリスト 
   * @return 存在するカタログアイテムの一覧
   */
  public List<CatalogItem> getExistCatalogItems(List<Long> catalogItemIds) {
    return this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
  }

  /**
   * 指定したカタログアイテム Id がリポジトリ内にすべて存在するかを取得します。
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

  private boolean existCatalogItemIdInItems(List<CatalogItem> items, long catalogItemId) {
    return items.stream().anyMatch(catalogItem -> catalogItem.getId() == catalogItemId);
  }
}
