package com.dressca.web.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Dressca アプリケーションを起動するための main クラスです。
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Dressca", description = "ECサイトDressca", version = "v1"))
@ComponentScan(basePackages = {"com.dressca"})
public class WebApplication {

  /**
   * Dressca アプリケーションを起動します。
   * 
   * @param args コマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }
}
