package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketItemEntityExample {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected String orderByClause;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected boolean distinct;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    protected List<Criteria> oredCriteria;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BasketItemEntityExample() {
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

        protected List<Criterion> basketIdCriteria;

        protected List<Criterion> catalogItemIdCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
            idCriteria = new ArrayList<>();
            basketIdCriteria = new ArrayList<>();
            catalogItemIdCriteria = new ArrayList<>();
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

        public List<Criterion> getBasketIdCriteria() {
            return basketIdCriteria;
        }

        protected void addBasketIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            basketIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addBasketIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            basketIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getCatalogItemIdCriteria() {
            return catalogItemIdCriteria;
        }

        protected void addCatalogItemIdCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            catalogItemIdCriteria.add(new Criterion(condition, value, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        protected void addCatalogItemIdCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            catalogItemIdCriteria.add(new Criterion(condition, value1, value2, "com.dressca.infrastructure.repository.mybatis.handler.UuidTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || idCriteria.size() > 0
                || basketIdCriteria.size() > 0
                || catalogItemIdCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(idCriteria);
                allCriteria.addAll(basketIdCriteria);
                allCriteria.addAll(catalogItemIdCriteria);
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

        public Criteria andBasketIdIsNull() {
            addCriterion("basket_id is null");
            return (Criteria) this;
        }

        public Criteria andBasketIdIsNotNull() {
            addCriterion("basket_id is not null");
            return (Criteria) this;
        }

        public Criteria andBasketIdEqualTo(UUID value) {
            addBasketIdCriterion("basket_id =", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdNotEqualTo(UUID value) {
            addBasketIdCriterion("basket_id <>", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdGreaterThan(UUID value) {
            addBasketIdCriterion("basket_id >", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdGreaterThanOrEqualTo(UUID value) {
            addBasketIdCriterion("basket_id >=", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdLessThan(UUID value) {
            addBasketIdCriterion("basket_id <", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdLessThanOrEqualTo(UUID value) {
            addBasketIdCriterion("basket_id <=", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdLike(UUID value) {
            addBasketIdCriterion("basket_id like", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdNotLike(UUID value) {
            addBasketIdCriterion("basket_id not like", value, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdIn(List<UUID> values) {
            addBasketIdCriterion("basket_id in", values, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdNotIn(List<UUID> values) {
            addBasketIdCriterion("basket_id not in", values, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdBetween(UUID value1, UUID value2) {
            addBasketIdCriterion("basket_id between", value1, value2, "basketId");
            return (Criteria) this;
        }

        public Criteria andBasketIdNotBetween(UUID value1, UUID value2) {
            addBasketIdCriterion("basket_id not between", value1, value2, "basketId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdIsNull() {
            addCriterion("catalog_item_id is null");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdIsNotNull() {
            addCriterion("catalog_item_id is not null");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdEqualTo(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id =", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdNotEqualTo(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id <>", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdGreaterThan(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id >", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdGreaterThanOrEqualTo(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id >=", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdLessThan(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id <", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdLessThanOrEqualTo(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id <=", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdLike(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id like", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdNotLike(UUID value) {
            addCatalogItemIdCriterion("catalog_item_id not like", value, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdIn(List<UUID> values) {
            addCatalogItemIdCriterion("catalog_item_id in", values, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdNotIn(List<UUID> values) {
            addCatalogItemIdCriterion("catalog_item_id not in", values, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdBetween(UUID value1, UUID value2) {
            addCatalogItemIdCriterion("catalog_item_id between", value1, value2, "catalogItemId");
            return (Criteria) this;
        }

        public Criteria andCatalogItemIdNotBetween(UUID value1, UUID value2) {
            addCatalogItemIdCriterion("catalog_item_id not between", value1, value2, "catalogItemId");
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