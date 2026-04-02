package com.dressca.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

/**
 * {@link WebApplication} の動作をテストするクラスです。
 */
@SpringBootTest
@ActiveProfiles("test")
class WebApplicationTests {

  @Test
  @DisplayName("コンテキストが正常に読み込まれるか確認")
  void contextLoads() {
  }
}
