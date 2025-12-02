package com.dressca.web.consumer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.consumer.WebApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

/**
 * {@link AssetsController} の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class AssetsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("testGet_01_正常系_存在するアセットコード")
  void testGet_01() throws Exception {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";

    // 期待する戻り値
    this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
  }

  @Test
  @DisplayName("testGet_02_異常系_存在しないアセットコード")
  void testGet_02() throws Exception {
    // テスト用の入力データ
    String assetCode = "NotExistAssetCode";

    this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isNotFound());
  }
}
