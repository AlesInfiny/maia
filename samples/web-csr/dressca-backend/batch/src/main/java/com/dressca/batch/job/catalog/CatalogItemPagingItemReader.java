package com.dressca.batch.job.catalog;

import com.dressca.applicationmodules.catalogmanagement.CatalogApplicationService;
import com.dressca.applicationmodules.catalogmanagement.entity.CatalogItem;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.database.AbstractPagingItemReader;

/**
 * カタログ管理コンテキストの公開 API を利用して、 CatalogItem をページング単位で読み取る Reader です。
 */
@RequiredArgsConstructor
public class CatalogItemPagingItemReader extends AbstractPagingItemReader<CatalogItem> {

  private final CatalogApplicationService catalogApplicationService;

  @Override
  protected void doReadPage() {
    List<CatalogItem> items = catalogApplicationService
        .getCatalogItemsWithPaging(getPage() * getPageSize(), getPageSize());
    if (results == null) {
      results = new CopyOnWriteArrayList<>();
    } else {
      results.clear();
    }
    results.addAll(items);
  }
}
