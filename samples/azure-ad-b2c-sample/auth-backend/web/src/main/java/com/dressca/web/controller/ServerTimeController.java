package com.dressca.web.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dressca.web.controller.dto.time.TimeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Azure AD B2C に接続するためのコントローラークラスです。
 */
@RestController
@Tag(name = "ServerTime", description = "認証不要でサーバーの現在時刻を取得します。")
@RequestMapping("/api/servertime")
public class ServerTimeController {

  /**
   * サーバーの現在時刻を取得します。
   * 
   * @return レスポンス。
   * @throws Exception 例外。
   */
  @Operation(summary = "サーバーの現在時刻を取得します。", description = "サーバーの現在時刻を取得します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeResponse.class)))
  })
  @GetMapping
  public ResponseEntity<TimeResponse> getServerTime() throws Exception {

    LocalDateTime nowDate = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    TimeResponse response = new TimeResponse(nowDate.format(dateTimeFormatter));
    return ResponseEntity.ok().body(response);
  }
}
