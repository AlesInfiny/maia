package com.dressca.cms.authentication.applicationcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.cms.authentication.applicationcore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.cms.systemcommon.util.ApplicationContextWrapper;

/**
 * {@link UserDetailsServiceImpl} の単体テストクラスです。
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@TestPropertySource(properties = "spring.messages.basename=i18n/messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Autowired
  private MessageSource messages;
  @Autowired
  private ApplicationContext applicationContext;
  private UserDetailsServiceImpl service;

  @BeforeEach
  void setUp() {
    // ApplicationContextWrapper を初期化
    ApplicationContextWrapper wrapper = new ApplicationContextWrapper();
    wrapper.setApplicationContext(applicationContext);

    service = new UserDetailsServiceImpl(userRepository, messages);
  }

  @Test
  void testLoadUserByUsername_正常系_ユーザーが存在する場合UserDetailsが返される() {
    // Arrange
    String username = "test@example.com";
    UserDetails expectedUserDetails = User
        .withUsername(username)
        .password("password")
        .roles("USER")
        .build();
    when(userRepository.findByEmail(username)).thenReturn(expectedUserDetails);

    // Act
    UserDetails result = service.loadUserByUsername(username);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getUsername()).isEqualTo(username);
    verify(userRepository, times(1)).findByEmail(username);
  }

  @Test
  void testLoadUserByUsername_異常系_ユーザーが存在しない場合UsernameNotFoundExceptionがスローされる() {
    // Arrange
    String username = "nonexistent@example.com";
    when(userRepository.findByEmail(username)).thenReturn(null);

    // Act & Assert
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));
    verify(userRepository, times(1)).findByEmail(username);
  }
}
