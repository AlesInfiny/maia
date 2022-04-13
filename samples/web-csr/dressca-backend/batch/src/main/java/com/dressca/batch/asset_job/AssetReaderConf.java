package com.dressca.batch.asset_job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dressca.infrastructure.repository.jdbc.JdbcAssetRepository;
import com.dressca.infrastructure.repository.jdbc.entity.AssetEntity;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

/**
 * asset_job用Readerを定義するクラス.
 * Assetテーブルからデータ一覧を取得
 */
@Configuration
public class AssetReaderConf {

    @Autowired
    JdbcAssetRepository assetRepository;

    /**
     * Assetテーブルからデータ一覧を取得するreaderのbean.
     */
    @Bean
    public RepositoryItemReader<AssetEntity> assetReader() {
        RepositoryItemReader<AssetEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(assetRepository);

        // クエリメソッド指定
        reader.setMethodName("findAll");
        // クエリメソッドの引数指定(findallなのでなし)
        List<Object> arguments = new ArrayList<>();
        reader.setArguments(arguments);

        // 取得結果のソート
        Map<String, Direction> sort = new HashMap<>();
        sort.put("assetCode", Direction.ASC);
        reader.setSort(sort);

        return reader;
    }

}
