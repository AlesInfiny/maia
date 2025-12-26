package com.dressca.cms.announcement.applicationcore.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージとお知らせメッセージ履歴を表現するエンティティクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWithHistory implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * お知らせメッセージ。
   */
  private Announcement announcement;

  /**
   * お知らせメッセージ履歴のリスト。
   */
  private List<AnnouncementHistory> histories = new ArrayList<>();
}
