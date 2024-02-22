package com.dressca.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.web.controller.dto.auth.UserResponse;
import com.dressca.web.security.TokenDecoder;
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
@Tag(name = "Auth", description = "認証済みユーザのユーザIDを取得するAPI")
@RequestMapping("/api/auth")
public class UserController {

  /**
   * ログイン時のメッセージ取得。
   * 
   * @return レスポンス
   * @throws Exception 例外
   */
  @Operation(summary = "ログインに成功したユーザIDを取得します.", description = "ログインに成功したユーザIDを取得します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))) })
  @GetMapping("/get")
  @CrossOrigin
  public ResponseEntity<UserResponse> getConnection(
      @RequestHeader(name = "Authorization", required = true) String accessToken) throws Exception {

    String userId = TokenDecoder.getObjectIdByAccessToken(accessToken);
    UserResponse response = new UserResponse(userId);
    return ResponseEntity.ok().body(response);
  }

}
