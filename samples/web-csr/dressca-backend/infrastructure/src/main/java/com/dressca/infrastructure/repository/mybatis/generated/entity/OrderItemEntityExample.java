package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderItemEntityExample {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected String orderByClause;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected boolean distinct;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected List<Criteria> oredCriteria;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OrderItemEntityExample() {
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

        protected List<Criterion> orderedCatalogItemIdCriteria;

        protected List<Criterion> orderIdCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
            idCriteria = new ArrayList<>();
            orderedCatalogItemIdCriteria = new ArrayList<>();
            orderIdCriteria = new ArrayList<>();
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

        public List<Criterion> getOrderedCatalogItemIdCriteria() {
            return orderedCatalogItemIdCriteria;
        }

        protected void addOrderedCatalogItemIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            orderedCatalogItemIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addOrderedCatalogItemIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            orderedCatalogItemIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getOrderIdCriteria() {
            return orderIdCriteria;
        }

        protected void addOrderIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            orderIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addOrderIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            orderIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || idCriteria.size() > 0
                || orderedCatalogItemIdCriteria.size() > 0
                || orderIdCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(idCriteria);
                allCriteria.addAll(orderedCatalogItemIdCriteria);
                allCriteria.addAll(orderIdCriteria);
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

        public Criteria andOrderedCatalogItemIdIsNull() {
            addCriterion("ordered_catalog_item_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdIsNotNull() {
            addCriterion("ordered_catalog_item_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdEqualTo(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id =", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotEqualTo(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id <>", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdGreaterThan(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id >", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdGreaterThanOrEqualTo(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id >=", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdLessThan(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id <", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdLessThanOrEqualTo(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id <=", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdLike(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id like", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotLike(UUID value) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id not like", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdIn(List<UUID> values) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id in", values, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotIn(List<UUID> values) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id not in", values, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdBetween(UUID value1, UUID value2) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id between", value1, value2, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotBetween(UUID value1, UUID value2) {
            addOrderedCatalogItemIdCriterion("ordered_catalog_item_id not between", value1, value2, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameIsNull() {
            addCriterion("ordered_product_name is null");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameIsNotNull() {
            addCriterion("ordered_product_name is not null");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameEqualTo(String value) {
            addCriterion("ordered_product_name =", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameNotEqualTo(String value) {
            addCriterion("ordered_product_name <>", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameGreaterThan(String value) {
            addCriterion("ordered_product_name >", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("ordered_product_name >=", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameLessThan(String value) {
            addCriterion("ordered_product_name <", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameLessThanOrEqualTo(String value) {
            addCriterion("ordered_product_name <=", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameLike(String value) {
            addCriterion("ordered_product_name like", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameNotLike(String value) {
            addCriterion("ordered_product_name not like", value, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameIn(List<String> values) {
            addCriterion("ordered_product_name in", values, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameNotIn(List<String> values) {
            addCriterion("ordered_product_name not in", values, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameBetween(String value1, String value2) {
            addCriterion("ordered_product_name between", value1, value2, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductNameNotBetween(String value1, String value2) {
            addCriterion("ordered_product_name not between", value1, value2, "orderedProductName");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeIsNull() {
            addCriterion("ordered_product_code is null");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeIsNotNull() {
            addCriterion("ordered_product_code is not null");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeEqualTo(String value) {
            addCriterion("ordered_product_code =", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeNotEqualTo(String value) {
            addCriterion("ordered_product_code <>", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeGreaterThan(String value) {
            addCriterion("ordered_product_code >", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ordered_product_code >=", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeLessThan(String value) {
            addCriterion("ordered_product_code <", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeLessThanOrEqualTo(String value) {
            addCriterion("ordered_product_code <=", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeLike(String value) {
            addCriterion("ordered_product_code like", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeNotLike(String value) {
            addCriterion("ordered_product_code not like", value, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeIn(List<String> values) {
            addCriterion("ordered_product_code in", values, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeNotIn(List<String> values) {
            addCriterion("ordered_product_code not in", values, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeBetween(String value1, String value2) {
            addCriterion("ordered_product_code between", value1, value2, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andOrderedProductCodeNotBetween(String value1, String value2) {
            addCriterion("ordered_product_code not between", value1, value2, "orderedProductCode");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIsNull() {
            addCriterion("unit_price is null");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIsNotNull() {
            addCriterion("unit_price is not null");
            return (Criteria) this;
        }

        public Criteria andUnitPriceEqualTo(BigDecimal value) {
            addCriterion("unit_price =", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotEqualTo(BigDecimal value) {
            addCriterion("unit_price <>", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceGreaterThan(BigDecimal value) {
            addCriterion("unit_price >", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("unit_price >=", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceLessThan(BigDecimal value) {
            addCriterion("unit_price <", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("unit_price <=", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIn(List<BigDecimal> values) {
            addCriterion("unit_price in", values, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotIn(List<BigDecimal> values) {
            addCriterion("unit_price not in", values, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unit_price between", value1, value2, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unit_price not between", value1, value2, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNull() {
            addCriterion("quantity is null");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNotNull() {
            addCriterion("quantity is not null");
            return (Criteria) this;
        }

        public Criteria andQuantityEqualTo(Integer value) {
            addCriterion("quantity =", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotEqualTo(Integer value) {
            addCriterion("quantity <>", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThan(Integer value) {
            addCriterion("quantity >", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("quantity >=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThan(Integer value) {
            addCriterion("quantity <", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("quantity <=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityIn(List<Integer> values) {
            addCriterion("quantity in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotIn(List<Integer> values) {
            addCriterion("quantity not in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityBetween(Integer value1, Integer value2) {
            addCriterion("quantity between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("quantity not between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(UUID value) {
            addOrderIdCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(UUID value) {
            addOrderIdCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(UUID value) {
            addOrderIdCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(UUID value) {
            addOrderIdCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(UUID value) {
            addOrderIdCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(UUID value) {
            addOrderIdCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(UUID value) {
            addOrderIdCriterion("order_id like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(UUID value) {
            addOrderIdCriterion("order_id not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<UUID> values) {
            addOrderIdCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<UUID> values) {
            addOrderIdCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(UUID value1, UUID value2) {
            addOrderIdCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(UUID value1, UUID value2) {
            addOrderIdCriterion("order_id not between", value1, value2, "orderId");
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