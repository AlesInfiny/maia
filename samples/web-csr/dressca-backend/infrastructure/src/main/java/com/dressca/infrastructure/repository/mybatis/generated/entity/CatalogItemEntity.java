package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CatalogItemEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal price;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String productCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long catalogCategoryId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long catalogBrandId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean isDeleted;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime rowVersion;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDescription() {
        return description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getPrice() {
        return price;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getProductCode() {
        return productCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getCatalogCategoryId() {
        return catalogCategoryId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogCategoryId(Long catalogCategoryId) {
        this.catalogCategoryId = catalogCategoryId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getCatalogBrandId() {
        return catalogBrandId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogBrandId(Long catalogBrandId) {
        this.catalogBrandId = catalogBrandId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getRowVersion() {
        return rowVersion;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRowVersion(OffsetDateTime rowVersion) {
        this.rowVersion = rowVersion;
    }
}