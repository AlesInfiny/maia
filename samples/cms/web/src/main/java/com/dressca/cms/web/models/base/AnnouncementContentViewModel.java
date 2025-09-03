package com.dressca.cms.web.models.base;

import org.hibernate.validator.constraints.URL;
import com.dressca.cms.web.models.validation.AnnouncementValidationGroup.AnnouncementStoreGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージコンテンツのビューモデルです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementContentViewModel {
  @NotNull(message = "{nullLanguageCode}", groups = AnnouncementStoreGroup.class)
  private String languageCode;

  @NotBlank(message = "{blankTitle}", groups = AnnouncementStoreGroup.class)
  @Size(max = 256, message = "{TooLongTitle}", groups = AnnouncementStoreGroup.class)
  private String title;

  @NotBlank(message = "{blankMessage}", groups = AnnouncementStoreGroup.class)
  @Size(max = 512, message = "{tooLongMessage}", groups = AnnouncementStoreGroup.class)
  private String message;

  @Size(max = 1024, message = "{tooLongLinkUrl}", groups = AnnouncementStoreGroup.class)
  @URL(message = "{invalidLinkUrl}", groups = AnnouncementStoreGroup.class)
  private String linkUrl;
}
