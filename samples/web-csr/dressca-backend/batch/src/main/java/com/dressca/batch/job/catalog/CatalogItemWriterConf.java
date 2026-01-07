package com.dressca.batch.job.catalog;

import com.dressca.applicationcore.catalog.CatalogItem;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
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
      @Value("#{jobParameters['output']}") String output) throws Exception {
    FlatFileItemWriter<CatalogItem> writer = new FlatFileItemWriter<>();
    FileSystemResource outputResource;
    if (output == null || "".equals(output)) {
      // 出力ファイル名が Job パラメータで設定されていない場合
      outputResource = new FileSystemResource("output/outputData.csv");
    } else {
      outputResource = new FileSystemResource("output/" + output);
    }
    writer.setResource(outputResource);
    writer.setAppendAllowed(true);

    BeanWrapperFieldExtractor<CatalogItem> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(new String[] {"name", "price", "productCode"});

    DelimitedLineAggregator<CatalogItem> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor);

    writer.setLineAggregator(lineAggregator);

    return writer;
  }
}
