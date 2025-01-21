package com.dressca.web.consumer.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.sql.SQLException;

/**
 * 開発環境で h2 データベースを立ち上げるためのクラスです。
 */
@Component
@Profile("local")
public class H2ServerLauncher {

  private Server tcpServer;

  /**
   * h2 サーバをサーバーモードで起動します。
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
   * h2 サーバを停止します。
   */
  @PreDestroy
  public void stop() {
    if (this.tcpServer != null) {
      this.tcpServer.stop();
    }
  }
}