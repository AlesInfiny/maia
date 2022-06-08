package com.dressca.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.dressca" })
@MapperScan(basePackages = { "com.dressca.infrastructure.repository.mybatis" })

/**
 * バッチアプリケーションのメインクラス
 */
public class BatchApplication {

    /**
     * バッチアプリケーションのメインメソッド
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        System.exit(SpringApplication.exit(app.run(args)));
    }

}
