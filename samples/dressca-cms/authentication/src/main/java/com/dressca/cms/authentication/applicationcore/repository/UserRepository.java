package com.dressca.cms.authentication.applicationcore.repository;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * ユーザー情報を取得するリポジトリインターフェース。
 */
public interface UserRepository {

  /**
   * 指定されたメールアドレスでユーザー情報を取得します。
   * 
   * @param email メールアドレス。
   * @return ユーザー情報。見つからない場合は null。
   */
  UserDetails findByEmail(String email);
}
