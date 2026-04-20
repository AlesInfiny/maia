package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;

public class CatalogItemAssetEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.asset_code")
    private String assetCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.catalog_item_id")
    private Long catalogItemId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.asset_code")
    public String getAssetCode() {
        return assetCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.asset_code")
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.catalog_item_id")
    public Long getCatalogItemId() {
        return catalogItemId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_item_assets.catalog_item_id")
    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
    }
}