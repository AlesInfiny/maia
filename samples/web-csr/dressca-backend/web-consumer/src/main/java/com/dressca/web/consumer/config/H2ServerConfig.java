package com.dressca.web.consumer.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.sql.SQLException;

/**
 * 開発環境でh2データベースを立ち上げるためのクラスです。
 */
@Component
@Profile("local")
public class H2ServerConfig {

  private Server tcpServer;

  /**
   * h2サーバをサーバーモードで起動します。
   */
  @PostConstruct
  public void start() {
    try {
      this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
    } catch (SQLException e) {
      System.out.println("H2 server is already started.");
    }
  }

  /**
   * h2サーバを停止します。
   */
  @PreDestroy
  public void stop() {
    if (this.tcpServer != null) {
      this.tcpServer.stop();
    }
  }
}