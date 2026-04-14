package com.dressca.web.admin.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dressca.web.admin.WebApplication;
import com.dressca.web.admin.filter.DummyUserInjectionFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * プロファイルごとの {@link DummyUserInjectionFilter} の動作を検証するテストクラスです。
 */
public class DummyUserInjectionFilterTest {

  private static final String LOCAL_PROFILE = "local";

  private static final String TEST_PROFILE = "test";

  private static final String ASSET_CODE = "b52dc7f712d94ca5812dd995bf926c04";

  @Test
  void testSecurityFilterChain_正常系_ローカルプロファイルではダミーフィルターが含まれる() {
    try (ConfigurableApplicationContext context = createContext(LOCAL_PROFILE)) {
      // Arrange
      boolean expected = true;

      // Act
      boolean actual = hasDummyUserInjectionFilter(context);

      // Assert
      assertEquals(expected, actual);
    }
  }

  @Test
  void testAuthorization_正常系_ローカルプロファイルでは未認証でも保護APIに到達できる() throws Exception {
    try (ConfigurableApplicationContext context = createContext(LOCAL_PROFILE)) {
      // Arrange
      MockMvc mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context)
          .apply(springSecurity()).build();

      // Act
      ResultActions response = mockMvc.perform(get("/api/assets/" + ASSET_CODE));

      // Assert
      response.andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    }
  }

  @Test
  void testSecurityFilterChain_異常系_テストプロファイルではダミーフィルターが含まれない() {
    try (ConfigurableApplicationContext context = createContext(TEST_PROFILE)) {
      // Arrange
      boolean expected = false;

      // Act
      boolean actual = hasDummyUserInjectionFilter(context);

      // Assert
      assertEquals(expected, actual);
    }
  }

  @Test
  void testAuthorization_異常系_テストプロファイルでは未認証アクセスが拒否される() throws Exception {
    try (ConfigurableApplicationContext context = createContext(TEST_PROFILE)) {
      // Arrange
      MockMvc mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context)
          .apply(springSecurity()).build();

      // Act
      ResultActions response = mockMvc.perform(get("/api/assets/" + ASSET_CODE));

      // Assert
      response.andExpect(status().isUnauthorized());
    }
  }

  /**
   * 指定したプロファイルでアプリケーションコンテキストを作成します。
   * 
   * @param profile 使用するプロファイル。
   * @return 作成されたアプリケーションコンテキスト。
   */
  private ConfigurableApplicationContext createContext(String profile) {
    return new SpringApplicationBuilder(WebApplication.class).profiles(profile)
        .properties("spring.main.web-application-type=servlet").run();
  }

  /**
   * アプリケーションコンテキスト内に {@link DummyUserInjectionFilter} が含まれているかを確認します。
   * 
   * @param context アプリケーションコンテキスト。
   * @return ダミーフィルターが含まれている場合は true、そうでない場合は false。
   */
  private boolean hasDummyUserInjectionFilter(ConfigurableApplicationContext context) {
    FilterChainProxy springSecurityFilterChain = context.getBean(FilterChainProxy.class);
    return springSecurityFilterChain.getFilterChains().stream()
        .filter(DefaultSecurityFilterChain.class::isInstance)
        .map(DefaultSecurityFilterChain.class::cast).flatMap(chain -> chain.getFilters().stream())
        .anyMatch(filter -> filter instanceof DummyUserInjectionFilter);
  }
}
