package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.util.UUID;

public class CatalogItemAssetEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String assetCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID catalogItemId;

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
    public UUID getCatalogItemId() {
        return catalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogItemId(UUID catalogItemId) {
        this.catalogItemId = catalogItemId;
    }
}