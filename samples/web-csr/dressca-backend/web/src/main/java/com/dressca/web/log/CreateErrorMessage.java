package com.dressca.web.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.util.ApplicationContextWrapper;

public class CreateErrorMessage {

  private static final String EXCEPTION_MESSAGE_SUFFIX_LOG = "log";
  private static final String EXCEPTION_MESSAGE_SUFFIX_FRONT = "front";
  private static final String PROPERTY_DELIMITER = ".";

  public static String createLogMessageStackTrace(Exception e, String exceptionId, String[] logMessageValue) {
    StringBuilder builder = new StringBuilder();
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    String code = String.join(PROPERTY_DELIMITER, exceptionId,
        EXCEPTION_MESSAGE_SUFFIX_LOG);
    String exceptionMessage = messageSource.getMessage(code, logMessageValue,
        Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage)
        .append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    e.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }

  public static String createLogErrorValue(String exceptionId, String[] logMessageValue) {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_LOG);
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(code, logMessageValue, Locale.getDefault());
  }

  public static String createFrontErrorValue(String exceptionId, String[] frontMessageValue) {
    String code = String.join(PROPERTY_DELIMITER, exceptionId, EXCEPTION_MESSAGE_SUFFIX_FRONT);
    MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
    return messageSource.getMessage(code, frontMessageValue, Locale.getDefault());
  }
}
