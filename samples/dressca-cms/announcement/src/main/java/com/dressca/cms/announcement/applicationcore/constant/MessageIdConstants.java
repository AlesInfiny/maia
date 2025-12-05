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
}
