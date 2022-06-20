package com.dressca.batch.job.catalog;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dressca.applicationcore.catalog.CatalogItem;

/**
 * CATALOG_ITEMテーブルからデータ一覧を取得するReaderの設定。
 */
@Configuration
public class CatalogItemReaderConf {

  @Autowired
  SqlSessionFactory sqlSessionFactory;

  /**
   * CATALOG_ITEMテーブルからデータ一覧を取得するReader。
   */
  @Bean
  public MyBatisPagingItemReader<CatalogItem> catalogItemReader() {
    return new MyBatisPagingItemReaderBuilder<CatalogItem>()
        .sqlSessionFactory(sqlSessionFactory)
        .queryId("com.dressca.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper.findWithPaging")
        .pageSize(10)
        .build();
  }

}
