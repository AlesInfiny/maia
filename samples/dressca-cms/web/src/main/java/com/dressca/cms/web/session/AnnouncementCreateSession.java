package com.dressca.cms.web.session;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * お知らせメッセージ登録画面のセッションオブジェクトです。
 */
@Component
@SessionScope
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementCreateSession implements Serializable {

  /**
   * お知らせメッセージのエンティティ。
   */
  private Announcement announcement;

  /**
   * セッションをクリアします。
   */
  public void clear() {
    this.announcement = null;
  }
}
