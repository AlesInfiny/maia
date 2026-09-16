package com.dressca.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dressca アプリケーションを起動するための main クラスです。例外ハンドラのテストで使用します。
 * web-admin や web-consumer の本番コードの WebApplication とクラス名（Bean 名）が重複しないように
 * TestWebApplication という名前にしています。
 */
@SpringBootApplication(scanBasePackages = {"com.dressca"})
@OpenAPIDefinition(info = @Info(title = "Dressca", description = "ECサイトDressca", version = "v1"))
public class TestWebApplication {

  /**
   * Dressca アプリケーションを起動します。
   *
   * @param args コマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(TestWebApplication.class, args);
  }
}
