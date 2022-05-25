package com.dressca.infrastructure.repository.jdbc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.applicationcore.catalog.CatalogItemAssetRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcCatalogItemAssetRepository;
import com.dressca.infrastructure.repository.jdbc.internal.JdbcCatalogItemRepository;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.jdbc.internal.entity.CatalogItemEntity;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CatalogItemAssetRepositoryImpl implements CatalogItemAssetRepository {
    private JdbcCatalogItemAssetRepository repository;
    // private JdbcCatalogItemRepository catalogItemRepository;

    @Override
    public List<CatalogItemAsset> findByCatalogItemIdIn(List<Long> catalogItemIds) {
        // TODO:未実装
        return null;
    }

}
