package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AnnouncementEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String category;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime postDateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime expireDateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer displayPriority;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime createdAt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime changedAt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean isDeleted;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCategory() {
        return category;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCategory(String category) {
        this.category = category;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getPostDateTime() {
        return postDateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPostDateTime(OffsetDateTime postDateTime) {
        this.postDateTime = postDateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getExpireDateTime() {
        return expireDateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExpireDateTime(OffsetDateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getDisplayPriority() {
        return displayPriority;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDisplayPriority(Integer displayPriority) {
        this.displayPriority = displayPriority;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setChangedAt(OffsetDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}