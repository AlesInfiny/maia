package com.dressca.applicationcore.authorization;

import java.util.List;

/**
 * ユーザーのセッション情報のインターフェース。
 */
public interface UserStore {

  public String getLoginUserName();

  public List<String> getLoginUserRoles();

  public boolean isInRole(String role);
}
