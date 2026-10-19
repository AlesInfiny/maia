package com.dressca.web.consumer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 買い物かご機能の結合テストクラスです。
 *
 * <p>コントローラーからデータベース（ H2 ）までを結合し、 API の呼び出しによって買い物かごの状態が変化することを検証します。</p>
 */
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class BasketItemControllerIntegrationTest {

  private static final String BUYER_COOKIE_NAME = "Dressca-Bid";
  private static final String BASKET_ITEMS_URL = "/api/basket-items";
  // 初期データ（ data.sql ）に登録されている陳列品 ID
  private static final String DISPLAY_ITEM_ID = "019b76da-a800-7006-8001-000000000001";
  private static final String NOT_EXIST_DISPLAY_ITEM_ID = "019b76da-a800-7006-8001-ffffffffffff";

  @Autowired
  private MockMvc mockMvc;

  private Cookie buyerCookie;

  @BeforeEach
  void setUp() {
    // テストごとに異なる購入者とすることで、テスト間で買い物かごの状態を共有しない
    this.buyerCookie = new Cookie(BUYER_COOKIE_NAME, UUID.randomUUID().toString());
  }

  @Test
  @DisplayName("testGetBasketItems_01_正常系_買い物かごが空の場合は空の一覧を返す")
  void testGetBasketItems_01() throws Exception {
    // Act
    ResultActions response = this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie));

    // Assert
    response.andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.buyerId").value(this.buyerCookie.getValue()))
        .andExpect(jsonPath("$.basketItems").isEmpty());
  }

  @Test
  @DisplayName("testPostBasketItem_01_正常系_追加した商品が買い物かごに含まれる")
  void testPostBasketItem_01() throws Exception {
    // Arrange
    String requestBody = postRequestBody(DISPLAY_ITEM_ID, 2);

    // Act
    ResultActions postResponse = this.mockMvc.perform(post(BASKET_ITEMS_URL)
        .cookie(this.buyerCookie).contentType(MediaType.APPLICATION_JSON).content(requestBody));

    // Assert
    postResponse.andExpect(status().isCreated());
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk()).andExpect(jsonPath("$.basketItems.length()").value(1))
        .andExpect(jsonPath("$.basketItems[0].displayItemId").value(DISPLAY_ITEM_ID))
        .andExpect(jsonPath("$.basketItems[0].quantity").value(2))
        .andExpect(jsonPath("$.basketItems[0].displayItem.id").value(DISPLAY_ITEM_ID));
  }

  @Test
  @DisplayName("testPostBasketItem_02_異常系_存在しない陳列品を追加すると400を返す")
  void testPostBasketItem_02() throws Exception {
    // Arrange
    String requestBody = postRequestBody(NOT_EXIST_DISPLAY_ITEM_ID, 1);

    // Act
    ResultActions response = this.mockMvc.perform(post(BASKET_ITEMS_URL)
        .cookie(this.buyerCookie).contentType(MediaType.APPLICATION_JSON).content(requestBody));

    // Assert
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
  }

  @Test
  @DisplayName("testPutBasketItems_01_正常系_買い物かご内の商品の数量を変更できる")
  void testPutBasketItems_01() throws Exception {
    // Arrange
    addItemToBasket(DISPLAY_ITEM_ID, 1);
    String requestBody =
        "[{\"displayItemId\":\"" + DISPLAY_ITEM_ID + "\",\"quantity\":5}]";

    // Act
    ResultActions putResponse = this.mockMvc.perform(put(BASKET_ITEMS_URL)
        .cookie(this.buyerCookie).contentType(MediaType.APPLICATION_JSON).content(requestBody));

    // Assert
    putResponse.andExpect(status().isNoContent());
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.basketItems[0].quantity").value(5));
  }

  @Test
  @DisplayName("testDeleteBasketItem_01_正常系_買い物かごから商品を削除できる")
  void testDeleteBasketItem_01() throws Exception {
    // Arrange
    addItemToBasket(DISPLAY_ITEM_ID, 1);

    // Act
    ResultActions deleteResponse = this.mockMvc
        .perform(delete(BASKET_ITEMS_URL + "/" + DISPLAY_ITEM_ID).cookie(this.buyerCookie));

    // Assert
    deleteResponse.andExpect(status().isNoContent());
    this.mockMvc.perform(get(BASKET_ITEMS_URL).cookie(this.buyerCookie))
        .andExpect(status().isOk()).andExpect(jsonPath("$.basketItems").isEmpty());
  }

  @Test
  @DisplayName("testDeleteBasketItem_02_異常系_買い物かごに存在しない商品を削除すると404を返す")
  void testDeleteBasketItem_02() throws Exception {
    // Act
    ResultActions response = this.mockMvc
        .perform(delete(BASKET_ITEMS_URL + "/" + DISPLAY_ITEM_ID).cookie(this.buyerCookie));

    // Assert
    response.andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
  }

  private void addItemToBasket(String displayItemId, int quantity) throws Exception {
    this.mockMvc
        .perform(post(BASKET_ITEMS_URL).cookie(this.buyerCookie)
            .contentType(MediaType.APPLICATION_JSON)
            .content(postRequestBody(displayItemId, quantity)))
        .andExpect(status().isCreated());
  }

  private static String postRequestBody(String displayItemId, int quantity) {
    return "{\"displayItemId\":\"" + displayItemId + "\",\"addedQuantity\":" + quantity + "}";
  }
}
