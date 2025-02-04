package com.dressca.web.consumer.config;

import com.dressca.web.consumer.filter.BuyerIdFilter;
import com.dressca.web.consumer.security.CookieSettings;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dressca Web 用の設定クラスです。
 */
@Configuration
public class DresscaWebConfig {

  @Autowired
  private CookieSettings cookieSettings;

  /**
   * 購入者 ID のフィルターを設定します。
   * 
   * @return 購入者 ID のフィルター。
   */
  @Bean
  public FilterRegistrationBean<Filter> buyerIdFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new BuyerIdFilter(this.cookieSettings));
    return bean;
  }
}
