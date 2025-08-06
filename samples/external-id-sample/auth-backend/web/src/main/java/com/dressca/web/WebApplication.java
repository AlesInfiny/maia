package com.dressca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Microsoft Entra External ID を利用した認証機能を提供するアプリケーションを起動するための main クラスです。
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Microsoft Entra External ID ユーザー認証",
        description = "Microsoft Entra External ID を利用したユーザー認証機能を提供するサンプルアプリケーションです。",
        version = "v1"))
@ComponentScan(basePackages = {"com.dressca"})
public class WebApplication {

  /**
   * Microsoft Entra External ID を利用した認証機能を提供するアプリケーションを起動します。
   *
   * @param args コマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
