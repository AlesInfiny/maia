package com.dressca.cms.announcement.applicationcore.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.dressca.cms.announcement.applicationcore.constant.ExceptionIdConstants;
import com.dressca.cms.systemcommon.exception.LogicException;
import com.dressca.cms.systemcommon.exception.ValidationError;
import lombok.Getter;

/**
 * 非宣言的なバリデーションエラーが発生したことを表す例外です。
 */
@Getter
public class AnnouncementValidationException extends LogicException {

  /**
   * エラーメッセージのリスト。
   */
  private final List<ValidationError> errorMessages = new ArrayList<>();

  /**
   * お知らせメッセージのバリデーションエラーを指定して、{@link AnnouncementValidationException}
   * クラスのインスタンスを初期化します。
   * 
   * @param errorMessages バリデーションエラーのリスト。
   */
  public AnnouncementValidationException(List<ValidationError> errorMessages) {
    super(null, ExceptionIdConstants.E_VALIDATION_ERROR,
        new String[] { joinFieldNames(errorMessages) });
    this.errorMessages.addAll(errorMessages);
  }

  /**
   * ValidationError の fieldName をカンマ区切りで連結します。
   * 
   * @param errors バリデーションエラーのリスト。
   * @return カンマ区切りのフィールド名文字列。
   */
  private static String joinFieldNames(List<ValidationError> errors) {
    if (errors == null || errors.isEmpty()) {
      return "";
    }
    return errors.stream()
        .map(ValidationError::getFieldName)
        .filter(name -> name != null && !name.isBlank())
        .distinct()
        .collect(Collectors.joining(", "));
  }
}
