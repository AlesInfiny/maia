package com.dressca.web.admin.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.dressca.applicationcore.authorization.UserStore;

/**
 * ユーザのセッション情報。
 */
@Component
public class UserStoreImpl implements UserStore {

  /**
   * ログイン中のユーザー名を取得します。
   *
   * @return ログイン中のユーザー名。未ログインの場合、空文字。
   */
  public String loginUserName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getName();
    }
    return "";
  }

  /**
   * ログイン中のユーザーのロールを取得します。
   *
   * @return ログイン中のユーザーのロール。未ログインの場合、空文字
   */
  public String loginUserRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("");
    }
    return "";
  }

  /**
   * ログイン中のユーザーが指定したロールに属しているかどうか確認します。
   *
   * @return ログインしている場合true。未ログインの場合false。
   */
  public boolean isInRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
          .anyMatch(roles -> roles.contains(role));
    }
    return false;
  }
}