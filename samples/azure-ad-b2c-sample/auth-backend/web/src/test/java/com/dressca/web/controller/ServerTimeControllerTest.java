package com.dressca.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import com.dressca.web.WebApplication;

/**
 * {@link ServerTimeController} の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class ServerTimeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("testTime_01_正常系_現在時刻を取得")
  void testAuth_01() throws Exception {
    this.mockMvc.perform(get("/api/servertime")).andExpect(status().isOk())
        .andExpect(jsonPath("$.serverTime").exists());
  }

}
