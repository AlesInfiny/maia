package com.dressca.web.config;

import com.dressca.web.filter.BuyerIdFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dressca Web用の設定クラス。
 */
@Configuration
public class DresscaWebConfig {

  /**
   * BuyerIdFilter の設定。
   * 
   * @return BuyerIdFilter
   */
  @Bean
  public FilterRegistrationBean<Filter> buyerIdFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new BuyerIdFilter());
    return bean;
  }
}
