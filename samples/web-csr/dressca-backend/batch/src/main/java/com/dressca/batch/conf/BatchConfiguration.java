
package com.dressca.batch.conf;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.batch.catalog_item_job.CatalogItemProcessor;
import com.dressca.batch.catalog_item_job.JobCompletionNotificationListener;
import com.dressca.batch.catalog_item_tasklet_job.CatalogItemTasklet;

/**
 * Jobの定義と各種設定を行うクラス
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /**
     * catalogItem_tasklet_job用のstepの設定.
     * 
     * @param catalogItemTasklet ステップで実行するTasklet
     */
    @Bean
    public Step catalogItem_tasklet_step1(CatalogItemTasklet catalogItemTasklet) {
        return stepBuilderFactory.get("catalogItem_tasklet_step1")
                .tasklet(catalogItemTasklet)
                .build();
    }

    /**
     * catalogItem_tasklet_jobの設定.
     * 
     * @param step1 ジョブで実行するstep
     */
    @Bean
    public Job catalogItem_tasklet_job(@Qualifier("catalogItem_tasklet_step1") Step step1) {
        return jobBuilderFactory.get("catalogItem_tasklet_job").incrementer(new RunIdIncrementer()).start(step1)
                .build();
    }

    /**
     * catalogItem_job用のstepの設定.
     * 
     * @param catalogItemReader    ステップで実行するReader
     * @param catalogItemProcessor ステップで実行するProcessor
     * @param catalogItemWriter    ステップで実行するWriter
     */
    @Bean
    public Step catalogItem_step1(MyBatisPagingItemReader<CatalogItem> catalogItemReader,
            CatalogItemProcessor catalogItemProcessor,
            FlatFileItemWriter<CatalogItem> catalogItemWriter) {
        // 複数のProcessorを連結する場合は下記のようにCompositeItemProcessorを利用する
        // CompositeItemProcessor<CatalogItemEntity, CatalogItem> compositeProcessor =
        // new CompositeItemProcessor<CatalogItemEntity, CatalogItem>();
        // List<ItemProcessor<?, ?>> itemProcessors = new ArrayList<>();
        // itemProcessors.add(catalogItemProcessor);
        // itemProcessors.add(nextProcessor);
        // compositeProcessor.setDelegates(itemProcessors);
        return stepBuilderFactory.get("catalogItem_step1").<CatalogItem, CatalogItem>chunk(2)
                .reader(catalogItemReader)
                // .processor(compositeProcessor)
                .processor(catalogItemProcessor)
                .writer(catalogItemWriter)
                // .faultTolerant()
                // .skipLimit(10)
                // スキップ可能例外の指定（リトライ設定の場合は代わりにretryで指定する）
                // .skip(Exception.class)
                // スキップ不可例外の指定（リトライ設定の場合は代わりにnoRetryで指定する）
                // .noSkip(FileNotFoundException.class)
                .build();
    }

    /**
     * catalogItem_job用の設定.
     * 
     * @param listener 設定するLitener
     * @param step1    ジョブで実行するstep
     */
    @Bean
    public Job catalogItem_job(JobCompletionNotificationListener listener,
            @Qualifier("catalogItem_step1") Step step1) {
        // ジョブパラメータにrun.idを自動的に付与、未指定時自動でrun.idがインクリメントされる
        // ジョブパラメータの衝突を自動回避する設定
        return jobBuilderFactory.get("catalogItem_job").incrementer(new RunIdIncrementer()).listener(listener)
                .flow(step1)
                .end()
                .build();
    }

}
