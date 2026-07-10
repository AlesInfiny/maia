package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.util.UUID;

public class AssetEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String assetCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String assetType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAssetCode() {
        return assetCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAssetType() {
        return assetType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}