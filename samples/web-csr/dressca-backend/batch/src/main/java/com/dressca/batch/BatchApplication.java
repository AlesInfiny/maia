package com.dressca.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * バッチアプリケーションを起動するためのメインクラスです。
 */
@SpringBootApplication
public class BatchApplication {

  /**
   * バッチアプリケーションを起動します。
   * 
   * @param args コマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(BatchApplication.class);
    app.setWebApplicationType(WebApplicationType.NONE);
    System.exit(SpringApplication.exit(app.run(args)));
  }
}
