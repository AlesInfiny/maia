package com.dressca.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * バッチアプリケーションのメインクラス。
 */
@SpringBootApplication
public class BatchApplication {

  /**
   * バッチアプリケーションのメインメソッド。
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(BatchApplication.class);
    app.setWebApplicationType(WebApplicationType.NONE);
    System.exit(SpringApplication.exit(app.run(args)));
  }
}
