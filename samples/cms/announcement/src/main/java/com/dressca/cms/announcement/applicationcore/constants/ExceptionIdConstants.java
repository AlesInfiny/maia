package com.dressca.cms.announcement.applicationcore.constants;

/**
 * 例外 ID を管理する定数クラスです。
 */
public class ExceptionIdConstants {
  /** 掲載開始日時が掲載終了日時より未来の日付に設定されています。 */
  public static final String E_POST_DATE_AFTER_EXPIRE_DATE = "postDateAfterExpireDate";

  /** 不正な言語コードが指定されています。 */
  public static final String E_INVALID_LANGUAGE_CODE = "invalidLanguageCode";

  /** お知らせメッセージコンテンツのリストが空です。 */
  public static final String E_ANNOUNCEMENT_CONTENTS_LIST_IS_EMPTY =
      "announcementContentsListIsEmpty";

  /** 重複する言語コードが指定されています。 */
  public static final String E_DUPLICATE_LANGUAGE_CODE = "duplicateLanguageCode";

  /** お知らせメッセージのバリデーションエラーが発生しました。 */
  public static final String E_ANNOUNCEMENT_VALIDATION_ERROR = "announcementValidationError";
}
