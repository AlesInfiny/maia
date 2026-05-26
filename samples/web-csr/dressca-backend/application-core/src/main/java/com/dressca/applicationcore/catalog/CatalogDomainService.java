package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  public List<CatalogItem> getExistCatalogItems(List<UUID> catalogItemIds) {
    return this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
  }

  public boolean existAll(List<UUID> catalogItemIds) {
    List<CatalogItem> items = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<UUID> notExistCatalogItemIds = catalogItemIds.stream()
        .filter(catalogItemId -> !this.existCatalogItemIdInItems(items, catalogItemId))
        .collect(Collectors.toList());

    return notExistCatalogItemIds.isEmpty();
  }

  public boolean existCatalogBrand(UUID catalogBrandId) {
    return this.brandRepository.findById(catalogBrandId) != null;
  }

  public boolean existCatalogCategory(UUID catalogCategoryId) {
    return this.categoryRepository.findById(catalogCategoryId) != null;
  }

  public boolean existCatalogItem(UUID catalogItemId) {
    return this.catalogRepository.findById(catalogItemId) != null;
  }

  public boolean existCatalogItemIncludingDeleted(UUID catalogItemId) {
    return this.catalogRepository.findByIdIncludingDeleted(catalogItemId) != null;
  }

  private boolean existCatalogItemIdInItems(List<CatalogItem> items, UUID catalogItemId) {
    return items.stream().anyMatch(catalogItem -> catalogItem.getId().equals(catalogItemId));
  }
}
