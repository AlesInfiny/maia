package com.dressca.batch.asset_job;

import com.dressca.applicationcore.assets.Asset;

import org.springframework.batch.item.ItemProcessor;

/**
 * asset_jobでのAssetに対する変換（業務処理）をするProcessor
 */
public class AssetItemProcessor implements ItemProcessor<Asset, Asset> {

    /**
     * Assetに対する変換（業務処理）をするProcessor
     * assetCodeを出力用に変換("00"付与)する
     * 
     * @param asset 変換前Asset
     * @return 変換後のAsset
     */
    @Override
    public Asset process(final Asset asset) throws Exception {
        String assetCode = asset.getAssetCode();
        // コードの前に"00"を付与する。
        assetCode = "00" + assetCode;
        String assetType = asset.getAssetType();
        final Asset converted = new Asset(assetCode, assetType);

        return converted;
    }

}
