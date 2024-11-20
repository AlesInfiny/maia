package com.dressca.applicationcore.authorization;

/**
 * ユーザーのセッション情報のインターフェース。
 */
public interface UserStore {

  public String loginUserName();

  public String loginUserRole();

  public boolean isInRole(String role);
}
