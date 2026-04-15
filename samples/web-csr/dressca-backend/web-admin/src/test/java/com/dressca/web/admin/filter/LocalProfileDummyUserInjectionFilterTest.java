package com.dressca.web.admin.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.admin.WebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 開発環境（local プロファイル）での {@link DummyUserInjectionFilter} の動作を検証するテストクラスです。
 */
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class LocalProfileDummyUserInjectionFilterTest {

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testSecurityFilterChain_正常系_開発環境ではDummyUserInjectionFilterが含まれる() {
    // Arrange
    boolean expected = true;

    // Act
    boolean actual = hasDummyUserInjectionFilter();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void testAuthorization_正常系_開発環境では未認証でも保護されたAPIに到達できる() throws Exception {
    // Arrange
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    HttpStatus expectedStatus = HttpStatus.OK;
    String expectedContentType = MediaType.IMAGE_PNG_VALUE;

    // Act
    ResultActions response = mockMvc.perform(get("/api/assets/" + assetCode));

    // Assert
    response.andExpect(status().is(expectedStatus.value()))
        .andExpect(content().contentType(expectedContentType));
  }

  /**
   * セキュリティフィルターチェイン に {@link DummyUserInjectionFilter} が含まれているかを確認します。
   * 
   * @return {@link DummyUserInjectionFilter} が含まれている場合は true、そうでない場合は false。
   */
  private boolean hasDummyUserInjectionFilter() {
    return springSecurityFilterChain.getFilterChains().stream()
        .filter(DefaultSecurityFilterChain.class::isInstance)
        .map(DefaultSecurityFilterChain.class::cast).flatMap(chain -> chain.getFilters().stream())
        .anyMatch(filter -> filter instanceof DummyUserInjectionFilter);
  }
}
