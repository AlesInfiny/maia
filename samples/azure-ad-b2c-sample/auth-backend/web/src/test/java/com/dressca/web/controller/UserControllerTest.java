package com.dressca.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
 * {@link UserController}の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("testAuth_01_異常系_Headerが設定されていない場合エラー")
  void testAuth_01() throws Exception {
    this.mockMvc.perform(get("/api/user"))
        .andExpect(status().isUnauthorized());
  }
}
