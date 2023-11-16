package com.dressca.web.controlleradvice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

import com.dressca.web.controller.AssetsController;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.web.WebApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import java.util.Locale;
import org.springframework.context.MessageSource;

@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class ExceptionHandlerControllerAdviceTest {

  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String PROPERTY_DELIMITER = ".";
  private static final String MOCK_APPENDER_NAME = "MockAppender";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  AssetsController assetsController;

  @Mock
  private Appender mockAppender;

  @Captor
  private ArgumentCaptor<LogEvent> logCaptor;

  @BeforeEach
  public void setup() {
    // アプリケーションログメッセージを取得する設定
    // Appenderの初期化
    Mockito.reset(mockAppender);
    // Appenderの名前を設定
    Mockito.when(mockAppender.getName()).thenReturn(MOCK_APPENDER_NAME);
    // Appenderとして利用できる準備ができていることを設定（下2行）
    Mockito.when(mockAppender.isStarted()).thenReturn(true);
    Mockito.when(mockAppender.isStopped()).thenReturn(false);

    this.setLogLevel(Level.INFO);
  }

  private void setLogLevel(Level level) {
    // application.logのロガーを取り出し、Appenderの設定（mockAppenderにログを出力させる）を行う。
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    Configuration config = ctx.getConfiguration();
    LoggerConfig loggerConfig = config.getLoggerConfig(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

    // テスト毎にAppenderを設定するため、一度初期化する。
    loggerConfig.removeAppender(MOCK_APPENDER_NAME);

    loggerConfig.setLevel(level);
    loggerConfig.addAppender(mockAppender, level, null);
    ctx.updateLoggers();
  }

  @Test
  @DisplayName("testException_01_正常系_その他の業務エラーをステータースコード500で返却する")
  void testException_01() throws LogicException {
    // テスト用の入力データ
    String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
    // 期待値の設定
    String exceptionId = ExceptionIdConstant.E_ASSET0001;
    String logMessageValue[] = { assetCode };
    // モックの戻り値設定
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new AssetNotFoundException(assetCode));
    try {
      // APIの呼び出しとエラー時のレスポンスであることの確認
      this.mockMvc.perform(get("/api/assets/" + assetCode))
          .andExpect(status().isInternalServerError())
          .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
      // アプリケーションログのメッセージの確認
      Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
      assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
      assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
          .startsWith(createLogMessage(exceptionId, logMessageValue));
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
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new SystemException(new AssetNotFoundException(assetCode), exceptionId, frontMessageValue,
            logMessageValue));
    try {
      // APIの呼び出しとエラー時のレスポンスであることの確認
      this.mockMvc.perform(get("/api/assets/" + assetCode))
          .andExpect(status().isInternalServerError())
          .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
      // アプリケーションログのメッセージの確認
      Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
      assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
      assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
          .startsWith(createLogMessage(exceptionId, logMessageValue));
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
    String logMessageValue[] = null;
    // モックの戻り値設定
    Mockito.when(assetsController.get(anyString()))
        .thenThrow(new RuntimeException());
    try {
      // APIの呼び出しとエラー時のレスポンスであることの確認
      this.mockMvc.perform(get("/api/assets/" + assetCode))
          .andExpect(status().isInternalServerError())
          .andExpect(content().json("{\"type\":\"" + exceptionId + "\"}"));
      // アプリケーションログのメッセージの確認
      Mockito.verify(mockAppender, times(1)).append(logCaptor.capture());
      assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
      assertThat(logCaptor.getValue().getMessage().getFormattedMessage())
          .startsWith(createLogMessage(exceptionId, logMessageValue));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  // エラー時のアプリケーションログ出力メッセージの先頭行を返す（2行目以降はエラーのスタックトレースのため可変）
  private String createLogMessage(String exceptionId, String[] logMessageValue) {
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    String exceptionMessage = messageSource.getMessage(code, logMessageValue, Locale.getDefault());
    return exceptionId + " " + exceptionMessage + SystemPropertyConstants.LINE_SEPARATOR;
  }
}
