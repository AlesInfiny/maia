
package com.dressca.batch.conf;

import java.util.ArrayList;
import java.util.List;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.batch.asset_job.AssetEntityConvertProcessor;
import com.dressca.batch.asset_job.AssetItemProcessor;
import com.dressca.batch.asset_job.JobCompletionNotificationListener;
import com.dressca.infrastructure.repository.jdbc.entity.AssetEntity;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * Jobの定義と各種設定を行うクラス
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.dressca" })
@EnableJdbcRepositories("com.dressca.infrastructure.repository.jdbc")
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // asset_job用設定
    @Bean
    public AssetEntityConvertProcessor assetEntityConvertProcessor() {
        return new AssetEntityConvertProcessor();
    }

    @Bean
    public AssetItemProcessor assetItemProcessor() {
        return new AssetItemProcessor();
    }

    @Bean
    public Step asset_step1(RepositoryItemReader<AssetEntity> assetReader, FlatFileItemWriter<Asset> assetWriter) {
        CompositeItemProcessor<AssetEntity, Asset> compositeProcessor = new CompositeItemProcessor<AssetEntity, Asset>();
        List<ItemProcessor<?, ?>> itemProcessors = new ArrayList<>();
        itemProcessors.add(assetEntityConvertProcessor());
        itemProcessors.add(assetItemProcessor());
        compositeProcessor.setDelegates(itemProcessors);
        return stepBuilderFactory.get("asset_step1").<AssetEntity, Asset>chunk(10).reader(assetReader)
                // .processor(assetProcessor())
                .processor(compositeProcessor)
                .writer(assetWriter).build();
    }

    @Bean
    public Job asset_job(JobCompletionNotificationListener listener, @Qualifier("asset_step1") Step step1) {
        return jobBuilderFactory.get("asset_job").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
                .end()
                .build();
    }

}
