package com.dressca.web.admin.authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
  @Override
  public String getLoginUserName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getName();
    }
    return "";
  }

  /**
   * ログイン中のユーザーのロールを取得します。
   *
   * @return ログイン中のユーザーのロールの配列。未ログインの場合、空の配列。
   */
  @Override
  public List<String> getLoginUserRoles() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());
      return roles;
    }
    return new ArrayList<>();
  }

  /**
   * ログイン中のユーザーが指定したロールに属しているかどうか確認します。
   *
   * @return ロールに属している場合true。未ログインの場合false。
   */
  public boolean isInRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
          .anyMatch(roles -> roles.equals(role));
    }
    return false;
  }
}