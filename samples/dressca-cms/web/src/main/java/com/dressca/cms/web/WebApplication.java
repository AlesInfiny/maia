package com.dressca.cms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションの実行クラスです。
 */
@SpringBootApplication(scanBasePackages = { "com.dressca.cms" })
public class WebApplication {

  /**
   * アプリケーションのエントリーポイント。
   * 
   * @param args コマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
