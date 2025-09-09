package com.dressca.cms.announcement.applicationcore.exception;

import java.util.List;
import com.dressca.cms.announcement.applicationcore.constants.ExceptionIdConstants;
import com.dressca.cms.systemcommon.exception.LogicException;
import com.dressca.cms.systemcommon.exception.ValidationError;

/**
 * お知らせメッセージの非宣言的バリデーションエラーが発生したことを表す例外クラスです。
 */
public class AnnouncementValidationException extends LogicException {
  private List<ValidationError> validationErrors;

  /**
   * お知らせメッセージの非宣言的バリデーションエラー{@link AnnouncementValidationException}を初期化します。
   * 
   * @param validationErrors 非宣言的なバリデーションエラーのリスト 。
   */
  public AnnouncementValidationException(List<ValidationError> validationErrors) {
    super(null, ExceptionIdConstants.E_ANNOUNCEMENT_VALIDATION_ERROR, null, null);
    this.validationErrors = validationErrors;
  }

  public List<ValidationError> getValidationErrors() {
    return this.validationErrors;
  }
}
