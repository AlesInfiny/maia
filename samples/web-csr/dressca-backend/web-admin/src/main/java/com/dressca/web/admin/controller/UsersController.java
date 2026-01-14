package com.dressca.web.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.web.admin.controller.dto.user.GetLoginUserResponse;
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
 * {@link UserStore} の情報にアクセスする API コントローラです。
 */
@RestController
@Tag(name = "Users", description = "ログイン中のユーザーの情報にアクセスする API です。")
@RequestMapping("/api/users")
@PreAuthorize(value = "isAuthenticated()")
public class UsersController {

  @Autowired(required = false)
  private UserStore userStore;

  /**
   * ログイン中のユーザーの情報を取得します。
   * 
   * @return ユーザの情報。
   */
  @Operation(summary = "ログイン中のユーザーの情報を取得します。", description = "ログイン中のユーザーの情報を取得します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = GetLoginUserResponse.class))),
      @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @GetMapping
  public ResponseEntity<GetLoginUserResponse> getLoginUser() {

    GetLoginUserResponse response = new GetLoginUserResponse(this.userStore.getLoginUserName(),
        this.userStore.getLoginUserRoles());
    return ResponseEntity.ok().body(response);
  }
}
