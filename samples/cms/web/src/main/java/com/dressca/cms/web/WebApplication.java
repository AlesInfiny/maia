package com.dressca.cms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * CMS アプリケーションのアプリケーションクラスです。
 */
@SpringBootApplication(scanBasePackages = {"com.dressca.cms"})
@OpenAPIDefinition(
    info = @Info(title = "Dressca CMS", description = "Dressca CMS", version = "v1"))
public class WebApplication {
  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }
}
