package com.dressca.cms.web.models.base;

import com.dressca.cms.web.models.validation.AnnouncementValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

/**
 * お知らせコンテンツのビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnnouncementContentViewModel {

  /**
   * お知らせコンテンツ ID。
   */
  private UUID id;

  /**
   * お知らせメッセージ ID。
   */
  private UUID announcementId;

  /**
   * 言語コード。
   */
  @NotBlank(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.languageCodeIsRequired}")
  @NotBlank(groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.languageCodeIsRequired}")
  private String languageCode;

  /**
   * タイトル。
   */
  @NotBlank(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.titleIsRequired}")
  @NotBlank(groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.titleIsRequired}")
  @Size(max = 256, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.titleTooLong}")
  @Size(max = 256, groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.titleTooLong}")
  private String title;

  /**
   * 表示メッセージ。
   */
  @NotBlank(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.messageIsRequired}")
  @NotBlank(groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.messageIsRequired}")
  @Size(max = 512, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.messageTooLong}")
  @Size(max = 512, groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.messageTooLong}")
  private String message;

  /**
   * リンク先 URL。
   */
  @Size(max = 1024, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.linkUrlTooLong}")
  @Size(max = 1024, groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.linkUrlTooLong}")
  @URL(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.linkUrlIsInvalid}")
  @URL(groups = AnnouncementValidationGroup.Update.class, message = "{announcement.edit.linkUrlIsInvalid}")
  private String linkUrl;
}
