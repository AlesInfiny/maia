package com.dressca.web.controlleradvice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

import com.dressca.web.controller.AssetsController;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.web.WebApplication;
import com.dressca.web.constant.ProblemDetailsConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import java.util.Locale;

/**
 * {@link ExceptionHandlerControllerAdvice }の動作をテストするクラスです。
 */
@SpringJUnitConfig
@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("production")
public class ExceptionHandlerControllerAdviceTest {

    private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
    private static final String EXCEPTION_MESSAGE_SUFFIX_FRONT = "front";
    private static final String PROPERTY_DELIMITER = ".";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AssetsController assetsController;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> logCaptor;

    /**
     * 各テスト実施前のセットアップを行うメソッド。
     */
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setup() {
        // アプリケーションログメッセージを取得する設定
        // Appenderの初期化
        Mockito.reset(mockAppender);

        // ロガーの取得とAppenderの設定
        Mockito.when(mockAppender.isStarted()).thenReturn(true);

        this.setLogLevel(Level.INFO);
    }

    private void setLogLevel(Level level) {
        // application.logのロガーを取り出し、Appenderの設定（mockAppenderにログを出力させる）を行う。
        Logger logger = (Logger) LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

        // テスト毎にAppenderを設定するため、一度初期化する。
        logger.detachAppender(mockAppender);
        logger.setLevel(level);
        logger.addAppender(mockAppender);

        LoggerContext loggerContext = logger.getLoggerContext();
        loggerContext.start();
    }

    @Test
    @DisplayName("testException_01_正常系_その他の業務エラーをステータースコード500で返却する(本番環境)。")
    void testException_01() throws Exception {
        // テスト用の入力データ
        String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
        // 期待値の設定
        String exceptionId = ExceptionIdConstant.E_ASSET0001;
        String[] frontMessageValue = { assetCode };
        String[] logMessageValue = { assetCode };
        // モックの戻り値設定
        Mockito.when(assetsController.get(anyString()))
                .thenThrow(new AssetNotFoundException(assetCode));
        // APIの呼び出しとエラー時のレスポンスであることの確認
        this.mockMvc.perform(get("/api/assets/" + assetCode))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"title\":\"" + ProblemDetailsConstant.LOGIC_ERROR_TITLE + "\"}"))
                .andExpect(jsonPath("$.error." + exceptionId)
                        .value(createFrontErrorMessage(exceptionId, frontMessageValue)))
                .andExpect(jsonPath("$.detail").doesNotExist());
        // アプリケーションログのメッセージの確認
        Mockito.verify(mockAppender, times(1)).doAppend(logCaptor.capture());
        assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
        assertThat(logCaptor.getValue().getFormattedMessage())
                .startsWith(createLogMessage(exceptionId, logMessageValue));
    }

    @Test
    @DisplayName("testException_02_正常系_その他のシステムエラーをステータースコード500で返却する(本番環境)。")
    void testException_02() throws Exception {
        // テスト用の入力データ
        String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
        // 期待値の設定
        String exceptionId = ExceptionIdConstant.E_SHARE0000;
        String[] frontMessageValue = null;
        String[] logMessageValue = null;
        // モックの戻り値設定
        Mockito.when(assetsController.get(anyString()))
                .thenThrow(new SystemException(new AssetNotFoundException(assetCode),
                        exceptionId, frontMessageValue,
                        logMessageValue));
        // APIの呼び出しとエラー時のレスポンスであることの確認
        this.mockMvc.perform(get("/api/assets/" + assetCode))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"title\":\"" + ProblemDetailsConstant.SYSTEM_ERROR_TITLE + "\"}"))
                .andExpect(jsonPath("$.error." + exceptionId)
                        .value(createFrontErrorMessage(exceptionId, frontMessageValue)))
                .andExpect(jsonPath("$.detail").doesNotExist());
        // アプリケーションログのメッセージの確認
        Mockito.verify(mockAppender, times(1)).doAppend(logCaptor.capture());
        assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
        assertThat(logCaptor.getValue().getFormattedMessage())
                .startsWith(createLogMessage(exceptionId, logMessageValue));
    }

    @Test
    @DisplayName("testException_03_正常系_上記のいずれにも当てはまらない例外をステータースコード500で返却する(本番環境)。")
    void testException_03() throws Exception {
        // テスト用の入力データ
        String assetCode = "b52dc7f712d94ca5812dd995bf926c04";
        // 期待値の設定
        String exceptionId = ExceptionIdConstant.E_SHARE0000;
        String[] frontMessageValue = null;
        String[] logMessageValue = null;
        // モックの戻り値設定
        Mockito.when(assetsController.get(anyString()))
                .thenThrow(new RuntimeException());
        // APIの呼び出しとエラー時のレスポンスであることの確認
        this.mockMvc.perform(get("/api/assets/" + assetCode))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"title\":\"" + ProblemDetailsConstant.SYSTEM_ERROR_TITLE + "\"}"))
                .andExpect(jsonPath("$.error." + exceptionId)
                        .value(createFrontErrorMessage(exceptionId, frontMessageValue)))
                .andExpect(jsonPath("$.detail").doesNotExist());
        // アプリケーションログのメッセージの確認
        Mockito.verify(mockAppender, times(1)).doAppend(logCaptor.capture());
        assertThat(logCaptor.getValue().getLevel()).isEqualTo(Level.ERROR);
        assertThat(logCaptor.getValue().getFormattedMessage())
                .startsWith(createLogMessage(exceptionId, logMessageValue));
    }

    // エラー時のアプリケーションログ出力メッセージの先頭行を返す（2行目以降はエラーのスタックトレースのため可変）
    private String createLogMessage(String exceptionId, String[] logMessageValue) {
        MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
        String code = String.join(PROPERTY_DELIMITER, exceptionId,
                EXCEPTION_MESSAGE_SUFFIX_LOG);
        String exceptionMessage = messageSource.getMessage(code, logMessageValue,
                Locale.getDefault());
        return exceptionId + " " + exceptionMessage + SystemPropertyConstants.LINE_SEPARATOR;
    }

    // エラー時のフロントに出力するメッセージを返す
    private String createFrontErrorMessage(String exceptionId, String[] frontMessageValue) {
        String code = String.join(PROPERTY_DELIMITER, exceptionId,
                EXCEPTION_MESSAGE_SUFFIX_FRONT);
        MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
        return messageSource.getMessage(code, frontMessageValue,
                Locale.getDefault());
    }
}
