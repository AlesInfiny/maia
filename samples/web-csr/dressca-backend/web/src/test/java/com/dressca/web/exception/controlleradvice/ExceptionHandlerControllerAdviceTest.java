package com.dressca.web.exception.controlleradvice;

import com.dressca.web.controller.AssetsController;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.web.WebApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class ExceptionHandlerControllerAdviceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  AssetsController assetsController;
  // AssetApplicationService assetApplicationService;

  @Test
  @DisplayName("testException_01_正常系_その他の業務エラーをステータースコード500で返却する")
  void testException_01() throws LogicException {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = ExceptionIdConstant.E_ASSET0001;
    // モックの戻り値設定
    when(assetsController.get(anyString()))
      .thenThrow(new AssetNotFoundException(assetCode));
    try {
      this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  @DisplayName("testException_02_正常系_その他のシステムエラーをステータースコード500で返却する。")
  void testException_02() throws LogicException {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = ExceptionIdConstant.E_SHARE0000;
    String frontMessageValue[] = null;
    String logMessageValue[] = null;
    // モックの戻り値設定
    when(assetsController.get(anyString()))
      .thenThrow(new SystemException(new AssetNotFoundException(assetCode), exceptionId, frontMessageValue, logMessageValue));
    try {
      this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  @DisplayName("testException_03_正常系_上記のいずれにも当てはまらない例外をステータースコード500で返却する。")
  void testException_03() throws LogicException {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = ExceptionIdConstant.E_SHARE0000;
    // モックの戻り値設定
    when(assetsController.get(anyString()))
      .thenThrow(new RuntimeException());
    try {
      this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}



