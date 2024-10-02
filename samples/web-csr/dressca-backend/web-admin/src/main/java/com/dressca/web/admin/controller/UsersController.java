package com.dressca.web.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.web.admin.controller.dto.authorization.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン中のユーザーの情報を取得します。
 */
@RestController
@Tag(name = "Users", description = "ログイン中のユーザーの情報を取得します。")
@RequestMapping("/api/users")
@PreAuthorize("hasRole('${user.role}')")
public class UsersController {

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
  @GetMapping
  public ResponseEntity<UserResponse> getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    String roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .map(role -> role.replace("ROLE_", ""))
        .collect(Collectors.joining(" "));
    return ResponseEntity.ok().body(new UserResponse(userName, roles));
  }
}