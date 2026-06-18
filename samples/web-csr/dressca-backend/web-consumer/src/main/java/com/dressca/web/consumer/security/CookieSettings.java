package com.dressca.web.consumer.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * Cookie の設定を格納するクラスです。
 */
@ConfigurationProperties(prefix = "cookie.settings")
@Data
public class CookieSettings {
  private String sameSite = "Strict";
  private boolean httpOnly = true;
  private boolean secure = false;
  private int expiredDays = 1;
}
