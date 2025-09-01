package com.dressca.cms.web.models.base;

import org.hibernate.validator.constraints.URL;
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
  @NotNull(message = "言語を選択してください")
  private String languageCode;

  @NotNull(message = "タイトルを入力してください")
  @Size(max = 256, message = "タイトルは256文字以内で入力してください")
  private String title;

  @NotNull(message = "表示メッセージを入力してください")
  @Size(max = 512, message = "表示メッセージは512文字以内で入力してください")
  private String message;

  @Size(max = 1024, message = "リンク先 URL は1024文字で入力してください")
  @URL(message = "リンク先 URL はURLの形式で入力してください")
  private String linkUrl;
}
