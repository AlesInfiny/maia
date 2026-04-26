package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.util.UUID;

public class AnnouncementContentEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.id")
    private UUID id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.announcement_id")
    private UUID announcementId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.language_code")
    private String languageCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.title")
    private String title;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.message")
    private String message;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.link_url")
    private String linkUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.id")
    public UUID getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.id")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.announcement_id")
    public UUID getAnnouncementId() {
        return announcementId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.announcement_id")
    public void setAnnouncementId(UUID announcementId) {
        this.announcementId = announcementId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.language_code")
    public String getLanguageCode() {
        return languageCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.language_code")
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.title")
    public String getTitle() {
        return title;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.message")
    public String getMessage() {
        return message;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.message")
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.link_url")
    public String getLinkUrl() {
        return linkUrl;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: announcement_contents.link_url")
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}