
package com.dressca.batch.job;

import com.dressca.applicationmodules.catalogmanagement.entity.CatalogItem;
import com.dressca.batch.job.catalog.CatalogItemPagingItemReader;
import com.dressca.batch.job.catalog.CatalogItemProcessor;
import com.dressca.batch.job.tasklet.catalog.CatalogItemTasklet;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Job の定義と各種設定を行うクラスです。
 */
@Configuration
public class BatchConfiguration {

  /**
   * カタログアイテムのタスクレットジョブ（catalogItemTaskletJob） 用の step を設定します。
   *
   * @param jobRepository ジョブのリポジトリ。
   * @param transactionManager トランザクションマネージャー。
   * @param catalogItemTasklet ステップで実行する Tasklet 。
   * @return ステップ。
   */
  @Bean
  public Step catalogItemTaskletStep1(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, CatalogItemTasklet catalogItemTasklet) {
    return new StepBuilder(jobRepository).tasklet(catalogItemTasklet)
        .transactionManager(transactionManager).build();
  }

  /**
   * カタログアイテムのタスクレットジョブ（catalogItemTaskletJob） を設定します。
   *
   * @param jobRepository ジョブのリポジトリ。
   * @param step1 ジョブで実行する step 。
   * @return ジョブ。
   */
  @Bean
  public Job catalogItemTaskletJob(JobRepository jobRepository,
      @Qualifier("catalogItemTaskletStep1") Step step1) {
    return new JobBuilder(jobRepository)
        .incrementer(new RunIdIncrementer()).start(step1).build();
  }

  /**
   * カタログアイテムのジョブ（catalogItemJob） 用の step を設定します。
   * 
   * @param jobRepository ジョブのリポジトリ。
   * @param transactionManager トランザクションマネージャー。
   * @param catalogItemReader ステップで実行する Reader 。
   * @param catalogItemProcessor ステップで実行する Processor 。
   * @param catalogItemWriter ステップで実行する Writer 。
   * @return ステップ。
   */
  @Bean
  public Step catalogItemStep1(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, CatalogItemPagingItemReader catalogItemReader,
      CatalogItemProcessor catalogItemProcessor,
      FlatFileItemWriter<CatalogItem> catalogItemWriter) {
    // 複数の Processor を連結する場合は下記のように CompositeItemProcessor を利用する
    // CompositeItemProcessor<CatalogItemEntity, CatalogItem> compositeProcessor =
    // new CompositeItemProcessor<CatalogItemEntity, CatalogItem>();
    // List<ItemProcessor<?, ?>> itemProcessors = new ArrayList<>();
    // itemProcessors.add(catalogItemProcessor);
    // itemProcessors.add(nextProcessor);
    // compositeProcessor.setDelegates(itemProcessors);
    return new StepBuilder(jobRepository).<CatalogItem, CatalogItem>chunk(2)
        .reader(catalogItemReader)
        // .processor(compositeProcessor)
        .processor(catalogItemProcessor).writer(catalogItemWriter)
        .transactionManager(transactionManager)
        // .faultTolerant()
        // .skipLimit(10)
        // スキップ可能例外の指定（リトライ設定の場合は代わりに retry で指定する）
        // .skip(Exception.class)
        // スキップ不可例外の指定（リトライ設定の場合は代わりに noRetry で指定する）
        // .noSkip(FileNotFoundException.class)
        .build();
  }

  /**
   * カタログアイテムのジョブ（catalogItemJob） を設定します。
   *
   * @param listener 設定する Listener 。
   * @param jobRepository ジョブのリポジトリ。
   * @param step1 ジョブで実行する step 。
   * @return ジョブ。
   */
  @Primary
  @Bean
  public Job catalogItemJob(JobCompletionNotificationListener listener,
      JobRepository jobRepository, @Qualifier("catalogItemStep1") Step step1) {
    // ジョブパラメータに run.id を自動的に付与、未指定時自動で run.id がインクリメントされる
    // ジョブパラメータの衝突を自動回避する設定
    return new JobBuilder(jobRepository).incrementer(new RunIdIncrementer())
        .listener(listener).flow(step1).end().build();
  }
}
