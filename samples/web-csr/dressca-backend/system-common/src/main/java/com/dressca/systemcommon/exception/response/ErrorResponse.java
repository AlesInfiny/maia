package com.dressca.systemcommon.exception.response;

import java.util.Locale;

import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.util.ApplicationContextWrapper;

import org.springframework.context.MessageSource;

import lombok.Getter;
import lombok.Setter;

/**
 * エラーレスポンス。 フロントへのレスポンスボディにJSONでマッピングされるエラー情報のクラス
 */
@Getter
@Setter
public class ErrorResponse {
  /**
   * エラーコード。
   */
  private String type;

  /** エラーコードに対応するコメント。 */
  private String title;

  /** エラーメッセージ。 */
  private String detail;

  /** 項目チェックエラー用。 */
  // private Map<String, List<ItemCheckErrorDetail>> invalidParams;

  /** リクエストURI。 */
  private String instance;

  /** メッセージソース。 */
  private static MessageSource messageSource =
      (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);

  private static final String EXCEPTION_MESSAGE_SUFFIX_FRONT = "front";
  private static final String PROPERTY_DELIMITER = ".";

  /** システム例外用コンストラクタ。 */
  public ErrorResponse(SystemException e, String uri) {
    this.type = e.getExceptionId();
    this.title = e.getExceptionId();
    this.detail = getFrontErrorMessage(e.getExceptionId(), e.getFrontMessageValue());
    this.instance = uri;
  }

  /** 業務例外用コンストラクタ。 */
  public ErrorResponse(LogicException e, String uri) {
    this.type = e.getExceptionId();
    this.title = e.getExceptionId();
    this.detail = getFrontErrorMessage(e.getExceptionId(), e.getFrontMessageValue());
    this.instance = uri;
  }

  private String getFrontErrorMessage(String exceptionId, String[] frontMessageValue) {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_FRONT);
    return messageSource.getMessage(code, frontMessageValue, Locale.getDefault());
  }
}
