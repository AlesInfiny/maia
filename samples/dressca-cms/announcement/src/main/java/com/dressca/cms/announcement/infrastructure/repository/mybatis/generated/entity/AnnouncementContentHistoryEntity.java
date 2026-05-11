package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.util.UUID;

public class AnnouncementContentHistoryEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID announcementHistoryId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String languageCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String title;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String message;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String linkUrl;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getAnnouncementHistoryId() {
        return announcementHistoryId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAnnouncementHistoryId(UUID announcementHistoryId) {
        this.announcementHistoryId = announcementHistoryId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLanguageCode() {
        return languageCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTitle() {
        return title;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getMessage() {
        return message;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLinkUrl() {
        return linkUrl;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}