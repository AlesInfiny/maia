package com.dressca.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Azure AD B2Cに接続するためのコントローラークラス。
 */
@RestController
@Tag(name = "Auth", description = "アカウントのログイン情報にアクセスするAPI")
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private RestTemplate restTemplate;

  /**
   * ログイン時のメッセージ取得。
   * 
   * @return レスポンス
   * @throws Exception 例外
   */
  @Operation(summary = "ログインの成功を取得します.", description = "ログインの成功を取得します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))) })
  @GetMapping("/get")
  @CrossOrigin
  public ResponseEntity<String> getConnection() throws Exception {

    return ResponseEntity.ok().body("Login Success");
  }

}
