package com.dressca.cms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションの実行クラスです。
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.dressca.cms.web",
        "com.dressca.cms.announcement",
        "com.dressca.cms.authentication",
        "com.dressca.cms.systemcommon"
    })
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
