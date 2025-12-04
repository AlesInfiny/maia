package com.dressca.cms.web.models;

import java.util.ArrayList;
import java.util.List;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ登録画面のビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementCreateViewModel {

  /**
   * お知らせメッセージのビューモデル。
   */
  @Valid
  private AnnouncementViewModel announcement;

  /**
   * お知らせコンテンツのビューモデル一覧。
   */
  @Valid
  private List<AnnouncementContentViewModel> contents = new ArrayList<>();
}
