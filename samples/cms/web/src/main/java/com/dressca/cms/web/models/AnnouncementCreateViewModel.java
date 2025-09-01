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
 * お知らせメッセージ登録画面のビューモデルです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementCreateViewModel {
  @Valid
  private AnnouncementViewModel announcement = new AnnouncementViewModel();
  @Valid
  private List<AnnouncementContentViewModel> contents = new ArrayList<>();
}
