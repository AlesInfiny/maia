package com.dressca.systemcommon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContext を保持するラッパークラスです。
 */
@Component
public class ApplicationContextWrapper implements ApplicationContextAware {

  private static ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  /**
   * 指定されたクラスの Bean を取得します。
   * 
   * @param <T> 取得する Bean の型。
   * @param clazz 取得する Bean のクラス。
   * @return 指定されたクラスの Bean 。
   */
  public static <T> T getBean(Class<T> clazz) {
    return context.getBean(clazz);
  }

  /**
   * 指定された名前の Bean を取得します。
   * 
   * @param name 取得する Bean の名前。
   * @return 指定された名前の Bean 。
   */
  public static Object getBean(String name) {
    return context.getBean(name);
  }
}
