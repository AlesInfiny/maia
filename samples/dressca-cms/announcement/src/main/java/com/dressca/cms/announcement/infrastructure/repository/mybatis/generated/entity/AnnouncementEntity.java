package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AnnouncementEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.id")
    private UUID id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.category")
    private String category;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.post_date_time")
    private OffsetDateTime postDateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.expire_date_time")
    private OffsetDateTime expireDateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.display_priority")
    private Integer displayPriority;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.created_at")
    private OffsetDateTime createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.changed_at")
    private OffsetDateTime changedAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.is_deleted")
    private Boolean isDeleted;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.id")
    public UUID getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.id")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.category")
    public String getCategory() {
        return category;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.category")
    public void setCategory(String category) {
        this.category = category;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.post_date_time")
    public OffsetDateTime getPostDateTime() {
        return postDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.post_date_time")
    public void setPostDateTime(OffsetDateTime postDateTime) {
        this.postDateTime = postDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.expire_date_time")
    public OffsetDateTime getExpireDateTime() {
        return expireDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.expire_date_time")
    public void setExpireDateTime(OffsetDateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.display_priority")
    public Integer getDisplayPriority() {
        return displayPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.display_priority")
    public void setDisplayPriority(Integer displayPriority) {
        this.displayPriority = displayPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.created_at")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.created_at")
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.changed_at")
    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.changed_at")
    public void setChangedAt(OffsetDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.is_deleted")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcements.is_deleted")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}