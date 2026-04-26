package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AnnouncementHistoryEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.id")
    private UUID id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.announcement_id")
    private UUID announcementId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.category")
    private String category;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.post_date_time")
    private OffsetDateTime postDateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.expire_date_time")
    private OffsetDateTime expireDateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.display_priority")
    private Integer displayPriority;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.created_at")
    private OffsetDateTime createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.changed_by")
    private String changedBy;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.operation_type")
    private Integer operationType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.id")
    public UUID getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.id")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.announcement_id")
    public UUID getAnnouncementId() {
        return announcementId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.announcement_id")
    public void setAnnouncementId(UUID announcementId) {
        this.announcementId = announcementId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.category")
    public String getCategory() {
        return category;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.category")
    public void setCategory(String category) {
        this.category = category;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.post_date_time")
    public OffsetDateTime getPostDateTime() {
        return postDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.post_date_time")
    public void setPostDateTime(OffsetDateTime postDateTime) {
        this.postDateTime = postDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.expire_date_time")
    public OffsetDateTime getExpireDateTime() {
        return expireDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.expire_date_time")
    public void setExpireDateTime(OffsetDateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.display_priority")
    public Integer getDisplayPriority() {
        return displayPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.display_priority")
    public void setDisplayPriority(Integer displayPriority) {
        this.displayPriority = displayPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.created_at")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.created_at")
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.changed_by")
    public String getChangedBy() {
        return changedBy;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.changed_by")
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.operation_type")
    public Integer getOperationType() {
        return operationType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_history.operation_type")
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}