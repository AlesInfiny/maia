package com.dressca.cms.web.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import lombok.Data;

@SessionScope
@Component
@Data
public class AnnouncementCreateSession {
  private Announcement announcement;

  public void clear() {
    this.announcement = null;
  }
}
