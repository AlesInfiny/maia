package com.dressca.batch.job.catalog;

import com.dressca.applicationmodules.catalogmanagement.CatalogApplicationService;
import com.dressca.applicationmodules.catalogmanagement.entity.CatalogItem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * カタログアイテムのデータ一覧を取得する Reader の設定です。
 */
@Configuration
@RequiredArgsConstructor
public class CatalogItemReaderConf {

  private final CatalogApplicationService catalogApplicationService;

  /**
   * カタログアイテムのデータ一覧を取得する Reader を設定します。
   * 
   * @return カタログ管理コンテキストの公開 API を用いてページングされた {@link CatalogItem} を読み取るための Reader 。
   */
  @Bean
  public CatalogItemPagingItemReader catalogItemReader() {
    CatalogItemPagingItemReader reader =
        new CatalogItemPagingItemReader(catalogApplicationService);
    reader.setPageSize(10);
    return reader;
  }
}
