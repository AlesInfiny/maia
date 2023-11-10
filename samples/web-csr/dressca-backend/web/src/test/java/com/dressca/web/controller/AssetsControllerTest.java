package com.dressca.web.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.WebApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class AssetsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("testGet_01_正常系_存在するアセットコード")
  void testGet_01() {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";

    // 期待する戻り値
    try {
      this.mockMvc.perform(get("/api/assets/" + assetCode))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  @DisplayName("testGet_02_異常系_存在しないアセットコード")
  void testGet_02() {
    // テスト用の入力データ
    String assetCode = "NotExistAssetCode";

    try {
      this.mockMvc.perform(get("/api/assets/" + assetCode))
          .andExpect(status().isNotFound());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}
