package com.dressca.web.consumer.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.dressca.web.consumer.security.CookieSettings;
import jakarta.servlet.http.Cookie;

/**
 * {@link BuyerIdFilter} の動作をテストするクラスです。
 */
public class BuyerIdFilterTest {

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain chain;

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

    // Set-Cookie ヘッダーの値が期待通りであることを確認
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
    CookieSettings cookieSettings = mock(CookieSettings.class);
    when(cookieSettings.isHttpOnly()).thenReturn(true);
    when(cookieSettings.isSecure()).thenReturn(true);
    when(cookieSettings.getExpiredDays()).thenReturn(7);
    when(cookieSettings.getSameSite()).thenReturn("None");

    // テスト対象の Filter を作成
    BuyerIdFilter filter = new BuyerIdFilter(cookieSettings);

    // doFilter の実行
    filter.doFilter(request, response, chain);
    String setCookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);

    // Set-Cookie ヘッダーの値が期待通りであることを確認
    assertNotNull(setCookieHeader);
    assertTrue(setCookieHeader.startsWith("Dressca-Bid="));
    assertTrue(setCookieHeader.contains("Path=/"));
    assertTrue(setCookieHeader.contains("HttpOnly"));
    assertTrue(setCookieHeader.contains("Secure"));
    assertTrue(setCookieHeader.contains("Max-Age=604800"));
    assertTrue(setCookieHeader.contains("SameSite=None"));
  }

  @Test
  @DisplayName("Dressca-Bid Cookie が有効な UUID の場合はその値が維持される")
  void testDoFilter_03() throws Exception {

    // 有効な Buyer ID を設定
    String validBuyerId = UUID.randomUUID().toString();
    this.request.setCookies(new Cookie("Dressca-Bid", validBuyerId));
    CookieSettings cookieSettings = new CookieSettings();
    BuyerIdFilter filter = new BuyerIdFilter(cookieSettings);

    // doFilter の実行
    filter.doFilter(request, response, chain);
    Cookie responseCookie = response.getCookie("Dressca-Bid");

    // Cookie が存在し、値が維持されていることを確認
    assertNotNull(responseCookie);
    String cookieBuyerId = responseCookie.getValue();
    assertEquals(validBuyerId, cookieBuyerId);
  }

  @Test
  @DisplayName("Dressca-Bid Cookie が無効な値の場合は新しい UUID が払い出される")
  void testDoFilter_04() throws Exception {

    // 無効な Buyer ID を設定
    String invalidBuyerId = "invalid-buyer-id";
    this.request.setCookies(new Cookie("Dressca-Bid", invalidBuyerId));
    CookieSettings cookieSettings = new CookieSettings();
    BuyerIdFilter filter = new BuyerIdFilter(cookieSettings);

    // doFilter の実行
    filter.doFilter(request, response, chain);
    Cookie responseCookie = response.getCookie("Dressca-Bid");
    String issuedBuyerId = responseCookie.getValue();

    // Cookie が存在し、無効な値から新しい UUID が払い出されていることを確認
    assertNotNull(responseCookie);
    assertNotEquals(invalidBuyerId, issuedBuyerId);
    assertNotNull(UUID.fromString(issuedBuyerId));
  }
}
