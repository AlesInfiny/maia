package com.dressca.cms.web.controller.advice;

import com.dressca.cms.announcement.applicationcore.exception.AnnouncementNotFoundException;
import com.dressca.cms.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.cms.systemcommon.constant.SystemPropertyConstants;
import com.dressca.cms.systemcommon.exception.SystemException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 例外ハンドリング機能を集約管理するコントローラーアドバイス。
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * リソースが見つからなかった場合の例外をハンドリングし、Not Found 画面に遷移します。
   * 
   * @param e     例外。
   * @param model モデル。
   * @return Not Found 画面のビュー名。
   */
  @ExceptionHandler({ AnnouncementNotFoundException.class, NoResourceFoundException.class })
  public String handleNotFoundException(Exception e, Model model) {
    apLog.info(e.getMessage());
    apLog.debug(ExceptionUtils.getStackTrace(e));
    return "not_found";
  }

  /**
   * システム例外をハンドリングし、システムエラー画面に遷移します。
   * 
   * @param e     システム例外。
   * @param model モデル。
   * @return システムエラー画面のビュー名。
   */
  @ExceptionHandler(SystemException.class)
  public String handleSystemException(SystemException e, Model model) {
    apLog.info(e.getMessage());
    apLog.debug(ExceptionUtils.getStackTrace(e));
    String errorCode = e.getExceptionId() != null ? e.getExceptionId() : CommonExceptionIdConstants.SYSTEM_ERROR;
    String occurredAt = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    model.addAttribute("errorCode", errorCode);
    model.addAttribute("occurredAt", occurredAt);

    return "error";
  }

  /**
   * その他の例外をハンドリングし、システムエラー画面に遷移します。
   * 
   * @param e     例外。
   * @param model モデル。
   * @return システムエラー画面のビュー名。
   */
  @ExceptionHandler(Exception.class)
  public String handleGeneralException(Exception e, Model model) {
    apLog.info(e.getMessage());
    apLog.debug(ExceptionUtils.getStackTrace(e));
    String occurredAt = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    model.addAttribute("errorCode", CommonExceptionIdConstants.SYSTEM_ERROR);
    model.addAttribute("occurredAt", occurredAt);

    return "error";
  }
}
