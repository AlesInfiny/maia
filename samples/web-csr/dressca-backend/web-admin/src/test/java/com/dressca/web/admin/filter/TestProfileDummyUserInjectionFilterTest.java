package com.dressca.web.admin.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.admin.WebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 開発環境以外（test プロファイル）での {@link DummyUserInjectionFilter} の動作を検証するテストクラスです。
 */
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestProfileDummyUserInjectionFilterTest {

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testSecurityFilterChain_異常系_テストプロファイルではダミーフィルターが含まれない() {
    // Arrange
    boolean expected = false;

    // Act
    boolean actual = hasDummyUserInjectionFilter();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void testAuthorization_異常系_テストプロファイルでは未認証アクセスが拒否される() throws Exception {
    // Arrange
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;

    // Act
    ResultActions response = mockMvc.perform(get("/api/assets/" + assetCode));

    // Assert
    response.andExpect(status().is(expectedStatus.value()));
  }

  /**
   * アプリケーションコンテキスト内に {@link DummyUserInjectionFilter} が含まれているかを確認します。
   * 
   * @return ダミーフィルターが含まれている場合は true、そうでない場合は false。
   */
  private boolean hasDummyUserInjectionFilter() {
    return springSecurityFilterChain.getFilterChains().stream()
        .filter(DefaultSecurityFilterChain.class::isInstance)
        .map(DefaultSecurityFilterChain.class::cast).flatMap(chain -> chain.getFilters().stream())
        .anyMatch(filter -> filter instanceof DummyUserInjectionFilter);
  }
}
