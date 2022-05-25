package com.dressca.infrastructure.repository.jdbc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcCatalogItemRepository;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

// @Repository
@AllArgsConstructor
public class CatalogRepositoryImpl implements CatalogRepository {

  private JdbcCatalogItemRepository repository;

  @Override
  public List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds) {
    List<CatalogItemEntity> entities = repository.findByCatalogCategoryIdIn(categoryIds);
    List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
        .map(this::toCatalogItem).collect(Collectors.toList());

    return catalogItems;
  }

  @Override
  public List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page,
      int pageSize) {
    Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
    List<CatalogItemEntity> entities = null;
    if (brandId != 0 && categoryId != 0) {
      entities = repository.findByCatalogBrandIdAndCatalogCategoryId(brandId, categoryId, pageable);
    } else if (brandId != 0) {
      entities = repository.findByCatalogBrandId(brandId, pageable);
    } else if (categoryId != 0) {
      entities = repository.findByCatalogCategoryId(categoryId, pageable);
    } else {
      // entities = repository.findAll(pageable);
      entities = StreamSupport.stream(repository.findAll().spliterator(), false)
          .collect(Collectors.toList());
    }

    List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
        .map(this::toCatalogItem).collect(Collectors.toList());

    return catalogItems;
  }

  @Override
  public List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds) {
    List<CatalogItemEntity> entities = repository.findByIdIn(catalogItemIds);
    List<CatalogItem> catalogItems = StreamSupport.stream(entities.spliterator(), false)
        .map(this::toCatalogItem).collect(Collectors.toList());

    return catalogItems;
  }

  @Override
  public int countByBrandIdAndCategoryId(long brandId, long categoryId) {
    return repository.countByCatalogBrandIdAndCatalogCategoryId(brandId, categoryId);
  }

  private CatalogItem toCatalogItem(CatalogItemEntity entity) {
    CatalogItem catalogItem =
        new CatalogItem(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(),
            entity.getProductCode(), entity.getCatalogCategoryId(), entity.getCatalogBrandId());
    // catalogItem.setAssets(
    // entity.getAssets().stream().map(this::toCatalogItemAsset).collect(Collectors.toList()));

    return catalogItem;
  }

  private CatalogItemAsset toCatalogItemAsset(CatalogItemAssetEntity entity) {
    return new CatalogItemAsset(entity.getCatalogItemId(), entity.getAssetCode());
  }
}
