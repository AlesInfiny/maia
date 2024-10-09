
package com.dressca.batch.job;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.batch.job.catalog.CatalogItemProcessor;
import com.dressca.batch.job.tasklet.catalog.CatalogItemTasklet;

/**
 * Jobの定義と各種設定を行うクラス。
 */
@Configuration
@ComponentScan(basePackages = { "com.dressca" })
@MapperScan(basePackages = { "com.dressca.infrastructure.repository.mybatis" })
public class BatchConfiguration {

  /**
   * catalogItem_tasklet_job用のstepの設定。
   *
   * @param catalogItemTasklet ステップで実行するTasklet
   */
  @Bean
  public Step catalogItem_tasklet_step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      CatalogItemTasklet catalogItemTasklet) {
    return new StepBuilder("catalogItem_tasklet_step1", jobRepository).tasklet(catalogItemTasklet, transactionManager)
        .build();
  }

  /**
   * catalogItem_tasklet_jobの設定。
   *
   * @param step1 ジョブで実行するstep
   */
  @Bean
  public Job catalogItem_tasklet_job(JobRepository jobRepository, @Qualifier("catalogItem_tasklet_step1") Step step1) {
    return new JobBuilder("catalogItem_tasklet_job", jobRepository).incrementer(new RunIdIncrementer())
        .start(step1).build();
  }

  /**
   * catalogItem_job用のstepの設定。
   * 
   * @param catalogItemReader    ステップで実行するReader
   * @param catalogItemProcessor ステップで実行するProcessor
   * @param catalogItemWriter    ステップで実行するWriter
   */
  @Bean
  public Step catalogItem_step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      MyBatisPagingItemReader<CatalogItem> catalogItemReader,
      CatalogItemProcessor catalogItemProcessor,
      FlatFileItemWriter<CatalogItem> catalogItemWriter) {
    // 複数のProcessorを連結する場合は下記のようにCompositeItemProcessorを利用する
    // CompositeItemProcessor<CatalogItemEntity, CatalogItem> compositeProcessor =
    // new CompositeItemProcessor<CatalogItemEntity, CatalogItem>();
    // List<ItemProcessor<?, ?>> itemProcessors = new ArrayList<>();
    // itemProcessors.add(catalogItemProcessor);
    // itemProcessors.add(nextProcessor);
    // compositeProcessor.setDelegates(itemProcessors);
    return new StepBuilder("catalogItem_step1", jobRepository).<CatalogItem, CatalogItem>chunk(2, transactionManager)
        .reader(catalogItemReader)
        // .processor(compositeProcessor)
        .processor(catalogItemProcessor).writer(catalogItemWriter)
        // .faultTolerant()
        // .skipLimit(10)
        // スキップ可能例外の指定（リトライ設定の場合は代わりにretryで指定する）
        // .skip(Exception.class)
        // スキップ不可例外の指定（リトライ設定の場合は代わりにnoRetryで指定する）
        // .noSkip(FileNotFoundException.class)
        .build();
  }

  /**
   * catalogItem_job用の設定。
   * 
   * @param listener 設定するListener
   * @param step1    ジョブで実行するstep
   */
  @Primary
  @Bean
  public Job catalogItem_job(JobCompletionNotificationListener listener,
      JobRepository jobRepository, @Qualifier("catalogItem_step1") Step step1) {
    // ジョブパラメータにrun.idを自動的に付与、未指定時自動でrun.idがインクリメントされる
    // ジョブパラメータの衝突を自動回避する設定
    return new JobBuilder("catalogItem_job", jobRepository).incrementer(new RunIdIncrementer())
        .listener(listener).flow(step1).end().build();
  }

}
