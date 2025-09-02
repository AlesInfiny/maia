package com.dressca.cms.announcement.applicationcore.constants;

/**
 * 例外 ID を管理する定数クラスです。
 */
public class ExceptionIdConstants {

  /** 言語コードに不正な値が指定されています。 */
  public static final String E_INVALID_LANGUAGE_CODE = "invalidLanguageCode";

  /** 掲載開始日時は掲載終了日時以降に設定してください。 */
  public static final String E_EXPIRE_DATE_AFTER_POST_DATE = "expireDateAfterPostDate";

  /** お知らせメッセージは 1 件以上登録してください */
  public static final String E_ANNOUNCEMENT_IS_EMPTY = "announcementIsEmpty";

  /** 同じ言語コードのお知らせメッセージが登録されています。 */
  public static final String E_DUPLICATE_LANGUAGE_CODE = "duplicateLanguageCode";

  /** お知らせメッセージのバリデーションエラーが発生しました。 */
  public static final String E_ANNOUNCEMENT_VALIDATION_ERROR = "announcementValidationError";
}
