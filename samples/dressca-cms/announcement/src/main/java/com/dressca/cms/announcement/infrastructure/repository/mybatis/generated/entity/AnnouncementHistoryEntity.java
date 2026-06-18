package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AnnouncementHistoryEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID announcementId;

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
    private String changedBy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer operationType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getAnnouncementId() {
        return announcementId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAnnouncementId(UUID announcementId) {
        this.announcementId = announcementId;
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
    public String getChangedBy() {
        return changedBy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getOperationType() {
        return operationType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}