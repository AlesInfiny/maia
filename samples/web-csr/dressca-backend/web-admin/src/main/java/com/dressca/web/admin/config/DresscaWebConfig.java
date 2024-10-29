package com.dressca.web.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.dressca.infrastructure.config.H2ServerConfig;

/**
 * Dressca Web用の設定クラス。
 */
@Configuration
public class DresscaWebConfig {

  @Autowired(required = false)
  public H2ServerConfig h2ServerConfig;

}
