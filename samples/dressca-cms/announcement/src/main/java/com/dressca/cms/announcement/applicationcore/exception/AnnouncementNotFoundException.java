package com.dressca.cms.announcement.applicationcore.exception;

import java.util.UUID;
import com.dressca.cms.announcement.applicationcore.constant.ExceptionIdConstants;
import com.dressca.cms.systemcommon.exception.LogicException;

/**
 * 指定したお知らせメッセージ ID に対応するお知らせメッセージが存在しなかったことを示す例外クラスです。
 */
public class AnnouncementNotFoundException extends LogicException {

  /**
   * お知らせメッセージ ID を指定して、{@link AnnouncementNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param announcementId お知らせメッセージ ID。
   */
  public AnnouncementNotFoundException(UUID announcementId) {
    super(null, ExceptionIdConstants.E_ANNOUNCEMENT_NOT_FOUND,
        new String[] {announcementId.toString()});
  }

}
