package com.dressca.web.consumer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.consumer.WebApplication;
import jakarta.servlet.http.Cookie;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 買い物かご機能の機能テストクラスです。
 *
 * <p>{@link BasketItemController} から アプリケーションサービス、リポジトリ、 DB までを組み合わせて、
 * 買い物かごの追加・取得・数量変更・削除の一連の動作を検証します。</p>
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class BasketItemControllerFeatureTest {

  private static final String BUYER_COOKIE_NAME = "Dressca-Bid";
  private static final String BASKET_ITEMS_URL = "/api/basket-items";
  // data.sql に登録されている陳列品 ID
  private static final String DISPLAY_ITEM_ID = "019b76da-a800-7006-8001-000000000001";
  private static final String NOT_EXIST_DISPLAY_ITEM_ID = "019b76da-a800-7006-8001-ffffffffffff";

  @Autowired
  private MockMvc mockMvc;

  private Cookie buyerCookie;

  @BeforeEach
  void setUp() {
    // DB はテスト間で共有されるため、テストごとに異なる購入者 ID を使用して買い物かごを分離する
    this.buyerCookie = new Cookie(BUYER_COOKIE_NAME, UUID.randomUUID().toString());
  }

  @Test
  @DisplayName("買い物かごに商品を追加すると、買い物かごの一覧で取得できる")
  void testPostAndGet_商品追加後に一覧で取得できる() throws Exception {
    // Act
    this.addItem(DISPLAY_ITEM_ID, 2).andExpect(status().isCreated());

    // Assert
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.buyerId").value(this.buyerCookie.getValue()))
        .andExpect(jsonPath("$.basketItems.length()").value(1))
        .andExpect(jsonPath("$.basketItems[0].displayItemId").value(DISPLAY_ITEM_ID))
        .andExpect(jsonPath("$.basketItems[0].quantity").value(2));
  }

  @Test
  @DisplayName("同じ商品を追加すると、数量が加算される")
  void testPost_同じ商品を追加すると数量が加算される() throws Exception {
    // Arrange
    this.addItem(DISPLAY_ITEM_ID, 1).andExpect(status().isCreated());

    // Act
    this.addItem(DISPLAY_ITEM_ID, 3).andExpect(status().isCreated());

    // Assert
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.basketItems.length()").value(1))
        .andExpect(jsonPath("$.basketItems[0].quantity").value(4));
  }

  @Test
  @DisplayName("存在しない陳列品を追加すると、 400 が返る")
  void testPost_存在しない陳列品を追加すると400() throws Exception {
    // Act & Assert
    this.addItem(NOT_EXIST_DISPLAY_ITEM_ID, 1).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("買い物かご内の商品の数量を変更できる")
  void testPut_数量を変更できる() throws Exception {
    // Arrange
    this.addItem(DISPLAY_ITEM_ID, 1).andExpect(status().isCreated());

    // Act
    this.mockMvc
        .perform(put(BASKET_ITEMS_URL).cookie(this.buyerCookie)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[{\"displayItemId\":\"" + DISPLAY_ITEM_ID + "\",\"quantity\":5}]"))
        .andExpect(status().isNoContent());

    // Assert
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.basketItems[0].quantity").value(5));
  }

  @Test
  @DisplayName("買い物かごから商品を削除できる")
  void testDelete_商品を削除できる() throws Exception {
    // Arrange
    this.addItem(DISPLAY_ITEM_ID, 1).andExpect(status().isCreated());

    // Act
    this.mockMvc
        .perform(delete(BASKET_ITEMS_URL + "/" + DISPLAY_ITEM_ID).cookie(this.buyerCookie))
        .andExpect(status().isNoContent());

    // Assert
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.basketItems.length()").value(0));
  }

  @Test
  @DisplayName("買い物かごにない商品を削除しようとすると、 404 が返る")
  void testDelete_買い物かごにない商品を削除すると404() throws Exception {
    // Arrange
    this.addItem(DISPLAY_ITEM_ID, 1).andExpect(status().isCreated());
    String otherDisplayItemId = "019b76da-a800-7006-8001-000000000002";

    // Act & Assert
    this.mockMvc
        .perform(delete(BASKET_ITEMS_URL + "/" + otherDisplayItemId).cookie(this.buyerCookie))
        .andExpect(status().isNotFound());
  }

  private ResultActions addItem(String displayItemId,
      int addedQuantity) throws Exception {
    return this.mockMvc.perform(post(BASKET_ITEMS_URL).cookie(this.buyerCookie)
        .contentType(MediaType.APPLICATION_JSON).content("{\"displayItemId\":\"" + displayItemId
            + "\",\"addedQuantity\":" + addedQuantity + "}"));
  }
}
