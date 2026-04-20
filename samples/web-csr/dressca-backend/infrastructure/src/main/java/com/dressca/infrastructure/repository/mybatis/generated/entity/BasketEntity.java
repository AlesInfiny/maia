package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;

public class BasketEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.buyer_id")
    private String buyerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: baskets.buyer_id")
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}