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
  private String languageCode;

  /**
   * タイトル。
   */
  @NotBlank(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.titleIsRequired}")
  @Size(max = 256, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.titleTooLong}")
  private String title;

  /**
   * 表示メッセージ。
   */
  @NotBlank(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.messageIsRequired}")
  @Size(max = 512, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.messageTooLong}")
  private String message;

  /**
   * リンク先 URL。
   */
  @Size(max = 1024, groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.linkUrlTooLong}")
  @URL(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.linkUrlIsInvalid}")
  private String linkUrl;
}
