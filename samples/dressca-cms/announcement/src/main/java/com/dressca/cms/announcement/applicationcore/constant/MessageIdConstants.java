package com.dressca.cms.announcement.applicationcore.constant;

/**
 * 業務メッセージ ID 用の定数クラスです。
 */
public final class MessageIdConstants {

  /** ページングされたお知らせメッセージ（ページ番号: {0}, ページサイズ: {1}）の取得を開始します。 */
  public static final String D_START_GET_PAGED_ANNOUNCEMENT_LIST = "announcementApplicationServiceStartGetPagedAnnouncementList";

  /** ページングされたお知らせメッセージ（ページ番号: {0}, ページサイズ: {1}）の取得を終了しました。 */
  public static final String D_END_GET_PAGED_ANNOUNCEMENT_LIST = "announcementApplicationServiceEndGetPagedAnnouncementList";

  /** お知らせメッセージとお知らせメッセージ履歴の追加を開始します。 */
  public static final String D_START_ADD_ANNOUNCEMENT_AND_HISTORY = "announcementApplicationServiceStartAddAnnouncementAndHistory";

  /** お知らせメッセージとお知らせメッセージ履歴の追加を終了しました。 */
  public static final String D_END_ADD_ANNOUNCEMENT_AND_HISTORY = "announcementApplicationServiceEndAddAnnouncementAndHistory";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージとお知らせメッセージ履歴の取得を開始します。 */
  public static final String D_START_GET_ANNOUNCEMENT_AND_HISTORIES_BY_ID = "announcementApplicationServiceStartGetAnnouncementAndHistoriesById";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージとお知らせメッセージ履歴の取得を終了しました。 */
  public static final String D_END_GET_ANNOUNCEMENT_AND_HISTORIES_BY_ID = "announcementApplicationServiceEndGetAnnouncementAndHistoriesById";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージの更新とお知らせメッセージ履歴の追加を開始します。 */
  public static final String D_START_UPDATE_ANNOUNCEMENT = "announcementApplicationServiceStartUpdateAnnouncement";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージの更新とお知らせメッセージ履歴の追加を終了しました。 */
  public static final String D_END_UPDATE_ANNOUNCEMENT = "announcementApplicationServiceEndUpdateAnnouncement";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージの削除とお知らせメッセージ履歴の追加を開始します。 */
  public static final String D_START_DELETE_ANNOUNCEMENT_AND_HISTORY = "announcementApplicationServiceStartDeleteAnnouncementAndHistory";

  /** お知らせメッセージ ID: {0} に対応するお知らせメッセージの削除とお知らせメッセージ履歴の追加を終了しました。 */
  public static final String D_END_DELETE_ANNOUNCEMENT_AND_HISTORY = "announcementApplicationServiceEndDeleteAnnouncementAndHistory";
}
