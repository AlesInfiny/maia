package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CatalogItemEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.description")
    private String description;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.price")
    private BigDecimal price;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.product_code")
    private String productCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_category_id")
    private Long catalogCategoryId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_brand_id")
    private Long catalogBrandId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.is_deleted")
    private Boolean isDeleted;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.row_version")
    private OffsetDateTime rowVersion;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.name")
    public void setName(String name) {
        this.name = name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.description")
    public String getDescription() {
        return description;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.price")
    public BigDecimal getPrice() {
        return price;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.price")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.product_code")
    public String getProductCode() {
        return productCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.product_code")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_category_id")
    public Long getCatalogCategoryId() {
        return catalogCategoryId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_category_id")
    public void setCatalogCategoryId(Long catalogCategoryId) {
        this.catalogCategoryId = catalogCategoryId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_brand_id")
    public Long getCatalogBrandId() {
        return catalogBrandId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.catalog_brand_id")
    public void setCatalogBrandId(Long catalogBrandId) {
        this.catalogBrandId = catalogBrandId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.is_deleted")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.is_deleted")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.row_version")
    public OffsetDateTime getRowVersion() {
        return rowVersion;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: catalog_items.row_version")
    public void setRowVersion(OffsetDateTime rowVersion) {
        this.rowVersion = rowVersion;
    }
}