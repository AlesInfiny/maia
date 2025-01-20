package com.dressca.web.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Dressca Web 用の設定クラスです。
 */
@Configuration
public class DresscaWebConfig {

  @Autowired(required = false)
  public H2ServerLauncher h2ServerLauncher;
}
