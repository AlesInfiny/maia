package com.dressca.infrastructure.repository.mybatis.generated.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemEntityExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_items
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_items
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_items
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public OrderItemEntityExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_items
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_items
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andOrderedCatalogItemIdEqualTo(Long value) {
            addCriterion("ordered_catalog_item_id =", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotEqualTo(Long value) {
            addCriterion("ordered_catalog_item_id <>", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdGreaterThan(Long value) {
            addCriterion("ordered_catalog_item_id >", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ordered_catalog_item_id >=", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdLessThan(Long value) {
            addCriterion("ordered_catalog_item_id <", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdLessThanOrEqualTo(Long value) {
            addCriterion("ordered_catalog_item_id <=", value, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdIn(List<Long> values) {
            addCriterion("ordered_catalog_item_id in", values, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotIn(List<Long> values) {
            addCriterion("ordered_catalog_item_id not in", values, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdBetween(Long value1, Long value2) {
            addCriterion("ordered_catalog_item_id between", value1, value2, "orderedCatalogItemId");
            return (Criteria) this;
        }

        public Criteria andOrderedCatalogItemIdNotBetween(Long value1, Long value2) {
            addCriterion("ordered_catalog_item_id not between", value1, value2, "orderedCatalogItemId");
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

        public Criteria andOrderIdEqualTo(Long value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Long value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Long value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Long value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Long> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Long> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Long value1, Long value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_items
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_items
     *
     * @mbg.generated
     */
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
}