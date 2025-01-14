package com.dressca.web.consumer.config;

import com.dressca.web.consumer.filter.BuyerIdFilter;
import com.dressca.web.consumer.security.CookieSettings;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dressca Web用の設定クラス。
 */
@Configuration
public class DresscaWebConfig {

  @Autowired
  private CookieSettings cookieSettings;

  @Autowired(required = false)
  public H2ServerLauncher h2ServerLauncher;

  /**
   * BuyerIdFilter の設定。
   * 
   * @return BuyerIdFilter
   */
  @Bean
  public FilterRegistrationBean<Filter> buyerIdFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new BuyerIdFilter(this.cookieSettings));
    return bean;
  }
}
