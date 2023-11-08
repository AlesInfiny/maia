package com.dressca.web.controller;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class HealthCheckTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("testGet_03_ヘルスチェック_サーバ正常動作確認")
    void testGet_serverCheck() {
      try {
        this.mockMvc.perform(get("/api/health/check"))
          .andExpect(status().isOk())
          .andExpect(content().json("{'status':'UP'}"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  
    @Test
    @DisplayName("testGet_04_ヘルスチェック_DB正常動作確認")
    void testGet_databaseCheck() {
      try {
        this.mockMvc.perform(get("/api/health/datasource"))
          .andExpect(status().isOk())
          .andExpect(content().json("{'status':'UP'}"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
