package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.util.UUID;

public class AnnouncementContentHistoryEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.id")
    private UUID id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.announcement_history_id")
    private UUID announcementHistoryId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.language_code")
    private String languageCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.title")
    private String title;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.message")
    private String message;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.link_url")
    private String linkUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.id")
    public UUID getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.id")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.announcement_history_id")
    public UUID getAnnouncementHistoryId() {
        return announcementHistoryId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.announcement_history_id")
    public void setAnnouncementHistoryId(UUID announcementHistoryId) {
        this.announcementHistoryId = announcementHistoryId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.language_code")
    public String getLanguageCode() {
        return languageCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.language_code")
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.title")
    public String getTitle() {
        return title;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.message")
    public String getMessage() {
        return message;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.message")
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.link_url")
    public String getLinkUrl() {
        return linkUrl;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_content_history.link_url")
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}