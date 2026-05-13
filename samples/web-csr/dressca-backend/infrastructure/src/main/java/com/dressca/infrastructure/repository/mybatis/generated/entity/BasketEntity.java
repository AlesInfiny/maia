package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;

public class BasketEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String buyerId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBuyerId() {
        return buyerId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}