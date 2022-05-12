package com.dressca.applicationcore.catalog;

import java.util.List;

public interface CatalogItemAssetRepository {

    List<CatalogItemAsset> findByCatalogItemIdIn(List<Long> catalogItemIds);

}
