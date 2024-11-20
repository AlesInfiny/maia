package com.dressca.web.consumer.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.dressca.web.consumer.WebApplication;
import com.dressca.web.consumer.security.CookieSettings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * {@link BuyerIdFilter}の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
public class BuyerIdFilterTest {

  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain chain;

  @BeforeEach
  void setup() {
    // モックの作成
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
    this.chain = new MockFilterChain();
  }

  @Test
  @DisplayName("構成ファイルの設定がない場合")
  void testDoFilter_01() throws Exception {

    // デフォルトの CookieSettings を呼び出す
    CookieSettings cookieSettings = new CookieSettings();

    // テスト対象の Filter を作成
    BuyerIdFilter filter = new BuyerIdFilter(cookieSettings);

    // doFilter の実行
    filter.doFilter(request, response, chain);
    String setCookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);

    // Set-Cookieヘッダーの値が期待通りであることを確認
    assertNotNull(setCookieHeader);
    assertTrue(setCookieHeader.startsWith("Dressca-Bid="));
    assertTrue(setCookieHeader.contains("Path=/"));
    assertTrue(setCookieHeader.contains("HttpOnly"));
    assertFalse(setCookieHeader.contains("Secure"));
    assertTrue(setCookieHeader.contains("Max-Age=86400"));
    assertTrue(setCookieHeader.contains("SameSite=Strict"));

  }

  @Test
  @DisplayName("構成ファイルの設定がある場合")
  void testDoFilter_02() throws Exception {

    // モックオブジェクトを作成
    CookieSettings cookieSettings = Mockito.mock(CookieSettings.class);
    Mockito.when(cookieSettings.isHttpOnly()).thenReturn(true);
    Mockito.when(cookieSettings.isSecure()).thenReturn(true);
    Mockito.when(cookieSettings.getExpiredDays()).thenReturn(7);
    Mockito.when(cookieSettings.getSameSite()).thenReturn("None");

    // テスト対象の Filter を作成
    BuyerIdFilter filter = new BuyerIdFilter(cookieSettings);

    // doFilter の実行
    filter.doFilter(request, response, chain);
    String setCookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);

    // Set-Cookieヘッダーの値が期待通りであることを確認
    assertNotNull(setCookieHeader);
    assertTrue(setCookieHeader.startsWith("Dressca-Bid="));
    assertTrue(setCookieHeader.contains("Path=/"));
    assertTrue(setCookieHeader.contains("HttpOnly"));
    assertTrue(setCookieHeader.contains("Secure"));
    assertTrue(setCookieHeader.contains("Max-Age=604800"));
    assertTrue(setCookieHeader.contains("SameSite=None"));

  }

}
