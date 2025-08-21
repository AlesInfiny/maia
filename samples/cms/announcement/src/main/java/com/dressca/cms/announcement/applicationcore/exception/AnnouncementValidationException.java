package com.dressca.cms.announcement.applicationcore.exception;

import com.dressca.cms.systemcommon.exception.LogicException;

/**
 * 非宣言的バリデーションエラーを表す例外クラスです。
 */
public class AnnouncementValidationException extends LogicException {


  public AnnouncementValidationException(Throwable cause, String exceptionId,
      String[] frontMessageValue, String[] logMessageValue) {
    super(cause, exceptionId, frontMessageValue, logMessageValue);
  }

}
