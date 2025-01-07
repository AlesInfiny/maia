package com.dressca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Dresscaアプリケーションを起動するためのmainクラスです。
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Dressca", description = "ECサイトDressca", version = "v1"))
@ComponentScan(basePackages = { "com.dressca" })
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }
}
