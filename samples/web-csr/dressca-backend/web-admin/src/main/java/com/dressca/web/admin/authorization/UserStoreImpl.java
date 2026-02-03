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
 * ユーザのセッション情報です。
 */
@Component
public class UserStoreImpl implements UserStore {

  @Override
  public String getLoginUserName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getName();
    }
    return "";
  }

  @Override
  public List<String> getLoginUserRoles() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      List<String> roles = authentication.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
      return roles;
    }
    return new ArrayList<>();
  }

  @Override
  public boolean isInRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
          .anyMatch(roles -> roles.equals(role));
    }
    return false;
  }
}
