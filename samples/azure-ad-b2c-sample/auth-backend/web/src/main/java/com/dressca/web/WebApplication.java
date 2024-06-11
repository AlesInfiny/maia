package com.dressca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Azure AD B2Cを利用した認証機能を提供するアプリケーションを起動するためのmainクラスです。
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Azure AD B2C ユーザー認証", description = "Azure AD B2Cを利用したユーザー認証機能を提供するサンプルアプリケーションです。", version = "v1"))
@ComponentScan(basePackages = { "com.dressca" })
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
