package com.dressca.cms.authentication.infrastructure;

import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * {@link UserDetails} を継承するクラス。 ユーザー情報を保持します。
 */
@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

  private final Integer id;
  private final String name;
  private final String email;
  private final String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    // Dressca CMS では、一意な識別子としてメールアドレスを使用するため、ここでメールアドレスを返します。
    return email;
  }

  public String getDisplayName() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
