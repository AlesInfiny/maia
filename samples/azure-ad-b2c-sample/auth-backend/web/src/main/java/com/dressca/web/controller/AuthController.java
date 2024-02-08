package com.dressca.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Azure AD B2Cに接続するためのコントローラークラス。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private RestTemplate restTemplate;

  /**
   * ログイン時のメッセージ取得。
   * 
   * @param accessToken トークン
   * @return レスポンス
   * @throws Exception 例外
   */
  @GetMapping("/get")
  @CrossOrigin
  public ResponseEntity<String> get() throws Exception {

    return ResponseEntity.ok().body("Login Success");
  }

}
