package com.dressca.web.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.web.admin.controller.dto.authorization.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン中のユーザーの情報を取得します。
 */
@RestController
@Tag(name = "Users", description = "ログイン中のユーザーの情報を取得します。")
@RequestMapping("/api/users")
public class UsersController {

  @Autowired
  private UserStore userStore;

  /**
   * ログイン中のユーザーの情報を取得します。
   * 
   * @return ユーザの情報。
   */
  @Operation(summary = "ログイン中のユーザーの情報を取得します。", description = "ユーザーの情報。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "401", description = "未認証エラー.", content = @Content)
  })
  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<UserResponse> getLoginUser() {

    UserResponse response = new UserResponse(
        this.userStore.loginUserName(),
        this.userStore.loginUserRole());
    return ResponseEntity.ok().body(response);

  }
}