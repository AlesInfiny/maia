package com.dressca.batch.asset_job;

import com.dressca.applicationcore.assets.Asset;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * asset_job用Writerを定義するクラス.
 * Assetの情報をローカルのCSVに出力する
 */

@Configuration
public class AssetWriterConf {
    public final Resource outputResource = new FileSystemResource("output/outputData.csv");

    /**
     * AssetデータをCSVに出力するwriterのbean.
     */

    @Bean
    public FlatFileItemWriter<Asset> csvflatFileItemWriter() {
        FlatFileItemWriter<Asset> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<Asset>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Asset>() {
                    {
                        setNames(new String[] { "assetCode", "assetType" });
                    }
                });
            }
        });
        return writer;
    }
}
