package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;

public class AssetEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_code")
    private String assetCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_type")
    private String assetType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_code")
    public String getAssetCode() {
        return assetCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_code")
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_type")
    public String getAssetType() {
        return assetType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: assets.asset_type")
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}