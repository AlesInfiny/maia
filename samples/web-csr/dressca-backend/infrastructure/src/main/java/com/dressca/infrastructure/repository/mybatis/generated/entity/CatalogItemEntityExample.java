package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogItemEntityExample {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected String orderByClause;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected boolean distinct;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected List<Criteria> oredCriteria;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public CatalogItemEntityExample() {
        oredCriteria = new ArrayList<>();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getOrderByClause() {
        return orderByClause;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public boolean isDistinct() {
        return distinct;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> idCriteria;

        protected List<Criterion> catalogCategoryIdCriteria;

        protected List<Criterion> catalogBrandIdCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
            idCriteria = new ArrayList<>();
            catalogCategoryIdCriteria = new ArrayList<>();
            catalogBrandIdCriteria = new ArrayList<>();
        }

        public List<Criterion> getIdCriteria() {
            return idCriteria;
        }

        protected void addIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            idCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            idCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getCatalogCategoryIdCriteria() {
            return catalogCategoryIdCriteria;
        }

        protected void addCatalogCategoryIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            catalogCategoryIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addCatalogCategoryIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            catalogCategoryIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getCatalogBrandIdCriteria() {
            return catalogBrandIdCriteria;
        }

        protected void addCatalogBrandIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            catalogBrandIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addCatalogBrandIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            catalogBrandIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || idCriteria.size() > 0
                || catalogCategoryIdCriteria.size() > 0
                || catalogBrandIdCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(idCriteria);
                allCriteria.addAll(catalogCategoryIdCriteria);
                allCriteria.addAll(catalogBrandIdCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
            allCriteria = null;
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(UUID value) {
            addIdCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(UUID value) {
            addIdCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(UUID value) {
            addIdCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(UUID value) {
            addIdCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(UUID value) {
            addIdCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(UUID value) {
            addIdCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(UUID value) {
            addIdCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(UUID value) {
            addIdCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<UUID> values) {
            addIdCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<UUID> values) {
            addIdCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(UUID value1, UUID value2) {
            addIdCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(UUID value1, UUID value2) {
            addIdCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andProductCodeIsNull() {
            addCriterion("product_code is null");
            return (Criteria) this;
        }

        public Criteria andProductCodeIsNotNull() {
            addCriterion("product_code is not null");
            return (Criteria) this;
        }

        public Criteria andProductCodeEqualTo(String value) {
            addCriterion("product_code =", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotEqualTo(String value) {
            addCriterion("product_code <>", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeGreaterThan(String value) {
            addCriterion("product_code >", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("product_code >=", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLessThan(String value) {
            addCriterion("product_code <", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLessThanOrEqualTo(String value) {
            addCriterion("product_code <=", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLike(String value) {
            addCriterion("product_code like", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotLike(String value) {
            addCriterion("product_code not like", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeIn(List<String> values) {
            addCriterion("product_code in", values, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotIn(List<String> values) {
            addCriterion("product_code not in", values, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeBetween(String value1, String value2) {
            addCriterion("product_code between", value1, value2, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotBetween(String value1, String value2) {
            addCriterion("product_code not between", value1, value2, "productCode");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdIsNull() {
            addCriterion("catalog_category_id is null");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdIsNotNull() {
            addCriterion("catalog_category_id is not null");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdEqualTo(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id =", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdNotEqualTo(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id <>", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdGreaterThan(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id >", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdGreaterThanOrEqualTo(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id >=", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdLessThan(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id <", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdLessThanOrEqualTo(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id <=", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdLike(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id like", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdNotLike(UUID value) {
            addCatalogCategoryIdCriterion("catalog_category_id not like", value, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdIn(List<UUID> values) {
            addCatalogCategoryIdCriterion("catalog_category_id in", values, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdNotIn(List<UUID> values) {
            addCatalogCategoryIdCriterion("catalog_category_id not in", values, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdBetween(UUID value1, UUID value2) {
            addCatalogCategoryIdCriterion("catalog_category_id between", value1, value2, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogCategoryIdNotBetween(UUID value1, UUID value2) {
            addCatalogCategoryIdCriterion("catalog_category_id not between", value1, value2, "catalogCategoryId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdIsNull() {
            addCriterion("catalog_brand_id is null");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdIsNotNull() {
            addCriterion("catalog_brand_id is not null");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdEqualTo(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id =", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdNotEqualTo(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id <>", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdGreaterThan(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id >", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdGreaterThanOrEqualTo(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id >=", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdLessThan(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id <", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdLessThanOrEqualTo(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id <=", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdLike(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id like", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdNotLike(UUID value) {
            addCatalogBrandIdCriterion("catalog_brand_id not like", value, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdIn(List<UUID> values) {
            addCatalogBrandIdCriterion("catalog_brand_id in", values, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdNotIn(List<UUID> values) {
            addCatalogBrandIdCriterion("catalog_brand_id not in", values, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdBetween(UUID value1, UUID value2) {
            addCatalogBrandIdCriterion("catalog_brand_id between", value1, value2, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andCatalogBrandIdNotBetween(UUID value1, UUID value2) {
            addCatalogBrandIdCriterion("catalog_brand_id not between", value1, value2, "catalogBrandId");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andRowVersionIsNull() {
            addCriterion("row_version is null");
            return (Criteria) this;
        }

        public Criteria andRowVersionIsNotNull() {
            addCriterion("row_version is not null");
            return (Criteria) this;
        }

        public Criteria andRowVersionEqualTo(OffsetDateTime value) {
            addCriterion("row_version =", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionNotEqualTo(OffsetDateTime value) {
            addCriterion("row_version <>", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionGreaterThan(OffsetDateTime value) {
            addCriterion("row_version >", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionGreaterThanOrEqualTo(OffsetDateTime value) {
            addCriterion("row_version >=", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionLessThan(OffsetDateTime value) {
            addCriterion("row_version <", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionLessThanOrEqualTo(OffsetDateTime value) {
            addCriterion("row_version <=", value, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionIn(List<OffsetDateTime> values) {
            addCriterion("row_version in", values, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionNotIn(List<OffsetDateTime> values) {
            addCriterion("row_version not in", values, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("row_version between", value1, value2, "rowVersion");
            return (Criteria) this;
        }

        public Criteria andRowVersionNotBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("row_version not between", value1, value2, "rowVersion");
            return (Criteria) this;
        }
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
    
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }
}