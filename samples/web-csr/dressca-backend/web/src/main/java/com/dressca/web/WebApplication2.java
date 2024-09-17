package com.dressca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Dresscaアプリケーションを起動するためのmainクラスです。
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.dressca" })
public class WebApplication2 {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication2.class, args);
  }

}
