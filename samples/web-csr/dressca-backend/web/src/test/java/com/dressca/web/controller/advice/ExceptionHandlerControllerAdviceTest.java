package com.dressca.web.controller.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.web.WebApplication;
import com.dressca.web.controller.AssetsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import java.util.Locale;

/**
 * {@link ExceptionHandlerControllerAdvice} の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("production")
public class ExceptionHandlerControllerAdviceTest {

  private static final String MOCK_APPENDER_NAME = "MockAppender";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  AssetsController assetsController;

  @Mock
  private Appender mockAppender;

  @Captor
  private ArgumentCaptor<LogEvent> logCaptor;

  /**
   * 各テスト実施前のセットアップを行うメソッド。
   */
  @BeforeEach
  public void setup() {
    // アプリケーションログメッセージを取得する設定
    // Appender の初期化
    Mockito.reset(mockAppender);
    // Appender の名前を設定
    Mockito.when(mockAppender.getName()).thenReturn(MOCK_APPENDER_NAME);
    // Appender として利用できる準備ができていることを設定（下 2 行）
    Mockito.when(mockAppender.isStarted()).thenReturn(true);
    Mockito.when(mockAppender.isStopped()).thenReturn(false);

    this.setLogLevel(Level.INFO);
  }

  private void setLogLevel(Level level) {
    // application.log のロガーを取り出し、 Appender の設定（ mockAppender にログを出力させる）を行う。
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    Configuration config = ctx.getConfiguration();
    LoggerConfig loggerConfig = config.getLoggerConfig(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

    // テスト毎に Appender を設定するため、一度初期化する。
    loggerConfig.removeAppender(MOCK_APPENDER_NAME);

    loggerConfig.setLevel(level);
    loggerConfig.addAppender(mockAppender, level, null);
    ctx.updateLoggers();
  }

  @Test
  @WithMockUser
  @DisplayName("testException_01_正常系_その他の業務エラーをステータースコード500で返却する(本番環境)。")
  void testException_01() throws Exception {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = CommonExceptionIdConstants.E_BUSINESS;
    String title = "想定外の業務エラーが発生しました。";
    String[] frontMessageValue = null;
    String[] logMessageValue = null;
    // モックの戻り値設定
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new LogicException(new AssetNotFoundException(assetCode), exceptionId,
            frontMessageValue, logMessageValue));
    // API の呼び出しとエラー時のレスポンスであることの確認
    this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"title\":\"" + title + "\"}"))
        .andExpect(jsonPath("$.exceptionId").value(exceptionId))
        .andExpect(jsonPath("$.exceptionValues").value(frontMessageValue))
        .andExpect(jsonPath("$.detail").doesNotExist());
    // アプリケーションログのメッセージの確認
    Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
    assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
    assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
        .startsWith(createLogMessage(exceptionId, logMessageValue));
  }

  @Test
  @WithMockUser
  @DisplayName("testException_02_正常系_その他のシステムエラーをステータースコード500で返却する(本番環境)。")
  void testException_02() throws Exception {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = CommonExceptionIdConstants.E_SYSTEM;
    String title = "想定外のシステムエラーが発生しました。";
    String[] frontMessageValue = null;
    String[] logMessageValue = null;
    // モックの戻り値設定
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new SystemException(new AssetNotFoundException(assetCode),
            exceptionId, frontMessageValue,
            logMessageValue));
    // API の呼び出しとエラー時のレスポンスであることの確認
    this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"title\":\"" + title + "\"}"))
        .andExpect(jsonPath("$.exceptionId").value(exceptionId))
        .andExpect(jsonPath("$.exceptionValues").value(frontMessageValue))
        .andExpect(jsonPath("$.detail").doesNotExist());
    // アプリケーションログのメッセージの確認
    Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
    assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
    assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
        .startsWith(createLogMessage(exceptionId, logMessageValue));
  }

  @Test
  @WithMockUser
  @DisplayName("testException_03_正常系_上記のいずれにも当てはまらない例外をステータースコード500で返却する(本番環境)。")
  void testException_03() throws Exception {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = CommonExceptionIdConstants.E_SYSTEM;
    String title = "想定外のシステムエラーが発生しました。";
    String[] frontMessageValue = null;
    String[] logMessageValue = null;
    // モックの戻り値設定
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new RuntimeException());
    // API の呼び出しとエラー時のレスポンスであることの確認
    this.mockMvc.perform(get("/api/assets/" + assetCode))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json("{\"title\":\"" + title + "\"}"))
        .andExpect(jsonPath("$.exceptionId").value(exceptionId))
        .andExpect(jsonPath("$.exceptionValues").value(frontMessageValue))
        .andExpect(jsonPath("$.detail").doesNotExist());
    // アプリケーションログのメッセージの確認
    Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
    assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
    assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
        .startsWith(createLogMessage(exceptionId, logMessageValue));
  }

  // エラー時のアプリケーションログ出力メッセージの先頭行を返す（ 2 行目以降はエラーのスタックトレースのため可変）
  private String createLogMessage(String exceptionId, String[] logMessageValue) {
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    String exceptionMessage = messageSource.getMessage(exceptionId, logMessageValue, Locale.getDefault());
    return exceptionId + " " + exceptionMessage + SystemPropertyConstants.LINE_SEPARATOR;
  }
}
