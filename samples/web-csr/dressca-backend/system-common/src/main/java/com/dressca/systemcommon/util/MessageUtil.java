package com.dressca.systemcommon.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ログやレスポンスでエラーメッセージを作成するためのクラスです。
 */
@Getter
@AllArgsConstructor
@Component
public class MessageUtil {

    /**
     * messages.propertiesからメッセージを取得するメソッドです。
     * 
     * @return Messages.propertiesのメッセージ
     */
    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, Locale.getDefault());
    }

    /**
     * messages.propertiesからメッセージを取得するメソッドです。
     * 
     * @return Messages.propertiesのメッセージ
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        MessageSource messageSource = (MessageSource) ApplicationContextWrapper
                .getBean(MessageSource.class);
        return messageSource.getMessage(code, args, locale);
    }
}
