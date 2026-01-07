package com.dressca.web.admin.config;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import jakarta.annotation.PreDestroy;
import java.sql.SQLException;

/**
 * 開発環境で H2 Database をサーバーモードで立ち上げるためのクラスです。
 */
@Component
@Profile("local")
public class H2ServerLauncher {

  private Server tcpServer;
  private final Logger apLog =
      LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * {@link H2ServerLauncher} クラスのインスタンスを初期化し、 H2 Database をサーバーモードで起動します。
   */
  public H2ServerLauncher() {
    try {
      this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists")
          .start();
    } catch (SQLException e) {
      apLog.info("H2 Database は既にサーバーモードで起動しています。");
    }
  }

  /**
   * インスタンスを破棄する際に H2 Database を停止します。
   */
  @PreDestroy
  public void stop() {
    if (this.tcpServer != null) {
      this.tcpServer.stop();
    }
  }
}
