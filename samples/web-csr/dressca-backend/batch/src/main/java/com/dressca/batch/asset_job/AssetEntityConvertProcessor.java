
package com.dressca.batch.asset_job;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.infrastructure.repository.jdbc.entity.AssetEntity;

import org.springframework.batch.item.ItemProcessor;

/**
 * AssetEntityからAssetへの変換するProcessor
 */
public class AssetEntityConvertProcessor implements ItemProcessor<AssetEntity, Asset> {

    /**
     * AssetEntityからAssetへの変換するprocess
     * 
     * @param asset Readerで読み込まれるAssetEntity
     * @return 変換後のAsset
     */
    @Override
    public Asset process(final AssetEntity asset) throws Exception {
        String assetCode = asset.getAssetCode();
        String assetType = asset.getAssetType();
        final Asset converted = new Asset(assetCode, assetType);

        return converted;
    }

}
