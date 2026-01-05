package com.dressca.cms.web.session;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * お知らせメッセージ削除のセッションオブジェクトです。
 */
@Component
@SessionScope
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDeleteSession implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * お知らせメッセージのエンティティ。
   */
  private Announcement announcement;

  /**
   * お知らせメッセージ履歴のリスト。
   */
  private List<AnnouncementHistory> histories = new ArrayList<>();

  /**
   * セッションをクリアします。
   */
  public void clear() {
    this.announcement = null;
    this.histories = new ArrayList<>();
  }
}
