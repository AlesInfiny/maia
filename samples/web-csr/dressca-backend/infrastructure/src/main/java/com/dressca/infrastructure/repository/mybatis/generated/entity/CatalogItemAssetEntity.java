package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;

public class CatalogItemAssetEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String assetCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long catalogItemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
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
    public Long getCatalogItemId() {
        return catalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
    }
}