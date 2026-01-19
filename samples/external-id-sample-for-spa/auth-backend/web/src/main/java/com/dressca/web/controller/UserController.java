package com.dressca.web.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.web.controller.dto.auth.UserResponse;
import com.dressca.web.security.UserIdThreadContextFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Microsoft Entra External ID に接続するためのコントローラークラスです。
 */
@RestController
@Tag(name = "Users", description = "認証済みユーザのユーザ ID を取得する API です。")
@RequestMapping("/api/users")
public class UserController {

  /**
   * ログイン時のメッセージを取得します。
   * 
   * @return レスポンス。
   * @throws Exception 例外。
   */
  @Operation(summary = "ログインに成功したユーザ ID を取得します。", description = "ログインに成功したユーザ ID を取得します。",
      security = {@SecurityRequirement(name = "Bearer")})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "401", description = "未認証エラー。",
          content = @Content(mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))})
  @GetMapping
  @PreAuthorize(value = "isAuthenticated()")
  public ResponseEntity<UserResponse> getUser() throws Exception {

    String userId = UserIdThreadContextFilter.threadLocalUserId.get();
    UserResponse response = new UserResponse(userId);
    return ResponseEntity.ok().body(response);
  }

}
