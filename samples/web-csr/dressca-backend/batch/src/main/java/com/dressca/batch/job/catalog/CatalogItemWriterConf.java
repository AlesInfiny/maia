package com.dressca.batch.job.catalog;

import com.dressca.applicationcore.catalog.CatalogItem;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * {@link CatalogItem} の情報をローカルの CSV に出力する Writer の設定です。
 */
@Configuration
public class CatalogItemWriterConf {

  /**
   * {@link CatalogItem} のデータを CSV に出力する Writer を設定します。
   * 
   * @param output 出力ファイル名。 Job パラメータから取得します。
   * @return フラットファイルに書き込む Writer 。
   * @throws Exception ジョブ実行時の例外。
   */
  @Bean
  @StepScope
  public FlatFileItemWriter<CatalogItem> csvFileItemWriter(
      @Value("#{jobParameters['output'] ?: null}") String output)
      throws Exception {
    String outputPath = (output == null || "".equals(output))
        ? "output/outputData.csv"
        : "output/" + output;

    return new FlatFileItemWriterBuilder<CatalogItem>()
        .name("catalogItemWriter")
        .resource(new FileSystemResource(outputPath))
        .append(true)
        .delimited()
        .delimiter(",")
        .names("name", "price", "productCode")
        .build();
  }
}
