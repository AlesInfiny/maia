package com.dressca.batch.job.catalog;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dressca.applicationcore.catalog.CatalogItem;

/**
 * CATALOG_ITEM テーブルからデータ一覧を取得する Reader の設定です。
 */
@Configuration
public class CatalogItemReaderConf {

  @Autowired
  SqlSessionFactory sqlSessionFactory;

  /**
   * CATALOG_ITEM テーブルからデータ一覧を取得する Reader を設定します。
   * 
   * @return MyBatis を用いてデータベースのページングされた {@link CatalogItem} を読み取るための Reader 。
   */
  @Bean
  public MyBatisPagingItemReader<CatalogItem> catalogItemReader() {
    return new MyBatisPagingItemReaderBuilder<CatalogItem>()
        .sqlSessionFactory(sqlSessionFactory)
        .queryId(
            "com.dressca.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper.findWithPaging")
        .pageSize(10)
        .build();
  }
}
