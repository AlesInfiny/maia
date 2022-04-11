package com.dressca.web.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetApplicationService;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.applicationcore.assets.AssetResourceInfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AssetsController.class)
public class AssetsControllerTest {

  // @Autowired
  // private MockMvc mockMvc;

  // @MockBean
  // private AssetApplicationService service;

  // @Test
  // @DisplayName("testGet_01_正常系_存在するアセットコード")
  // void testGet_01_正常系_存在するアセットコード() {
  //   // テスト用の入力データ
  //   String assetCode = "ExistAssetCode";

  //   // 期待する戻り値
  //   AssetResourceInfo assetResourceInfo = new AssetResourceInfo(new Asset(assetCode, "png"), new FileSystemResource("test"));
  //   try {
  //     when(service.getAssetResourceInfo(assetCode)).thenReturn(assetResourceInfo);
  //     this.mockMvc.perform(get("/api/assets/" + assetCode))
  //       .andDo(print())
  //       .andExpect(status().isOk())
  //       .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     fail();
  //   }
  // }

  // @Test
  // @DisplayName("testGet_02_異常系_存在しないアセットコード")
  // void testGet_02_異常系_存在しないアセットコード() {
  //   // テスト用の入力データ
  //   String assetCode = "NotExistAssetCode";

  //   try {
  //     when(service.getAssetResourceInfo(assetCode)).thenThrow(new AssetNotFoundException(assetCode));
  //     this.mockMvc.perform(get("/api/assets/" + assetCode))
  //       .andExpect(status().isNotFound());
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     fail();
  //   }
  // }
}
