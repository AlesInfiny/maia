package com.dressca.applicationcore.authorization;

import java.util.List;

/**
 * ユーザーのセッション情報のインターフェースです。
 */
public interface UserStore {

  /**
   * ログイン中のユーザー名を取得します。
   *
   * @return ログイン中のユーザー名。未ログインの場合、空文字。
   */
  public String getLoginUserName();

  /**
   * ログイン中のユーザーのロールを取得します。
   *
   * @return ログイン中のユーザーのロールの配列。未ログインの場合、空の配列。
   */
  public List<String> getLoginUserRoles();

  /**
   * ログイン中のユーザーが指定したロールに属しているかどうか確認します。
   *
   * @return ロールに属している場合 true 。未ログインの場合 false 。
   */
  public boolean isInRole(String role);
}
