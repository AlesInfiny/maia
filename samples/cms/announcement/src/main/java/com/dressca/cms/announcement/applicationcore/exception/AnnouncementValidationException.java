package com.dressca.cms.announcement.applicationcore.exception;

import java.util.List;
import com.dressca.cms.announcement.applicationcore.constants.ExceptionIdConstants;
import com.dressca.cms.systemcommon.exception.LogicException;

/**
 * お知らせメッセージの非宣言的バリデーションエラーを表す例外クラスです。
 */
public class AnnouncementValidationException extends LogicException {
  private List<AnnouncementValidationError> validationErrors;

  /**
   * お知らせメッセージの非宣言的バリデーションエラー{@link AnnouncementValidationException}を初期化します。
   * 
   * @param exceptionId 例外 ID 。
   */
  public AnnouncementValidationException(List<AnnouncementValidationError> validationErrors) {
    super(null, ExceptionIdConstants.E_ANNOUNCEMENT_VALIDATION_ERROR, null, null);
    this.validationErrors = validationErrors;
  }

  public List<AnnouncementValidationError> getValidationErrors() {
    return this.validationErrors;
  }
}
