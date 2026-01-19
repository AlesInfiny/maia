package com.dressca.cms.authentication.applicationcore;

import com.dressca.cms.authentication.applicationcore.constant.ExceptionIdConstants;
import com.dressca.cms.authentication.applicationcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * {@link UserDetailsService} を継承するクラス。 指定された名前のユーザー情報を返します。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final MessageSource messages;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Dressca CMS では、ログイン時にメールアドレスを使用するため、
    // UserRepository を使用してメールアドレスに対応するユーザー情報を取得します。
    UserDetails userDetails = userRepository.findByEmail(username);
    if (userDetails == null) {
      throw new UsernameNotFoundException(messages.getMessage(ExceptionIdConstants.E_USER_NOT_FOUND,
          new Object[] {username}, Locale.getDefault()));
    }
    return userDetails;
  }
}
