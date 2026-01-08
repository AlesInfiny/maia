package com.dressca.web.admin.controller;

import com.dressca.applicationcore.applicationservice.AssetApplicationService;
import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetNotFoundException;
import com.dressca.applicationcore.assets.AssetResourceInfo;
import com.dressca.applicationcore.assets.AssetTypes;
import com.dressca.systemcommon.exception.LogicException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * {@link Asset} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "Assets", description = "アセットの情報にアクセスする API です。")
@RequestMapping("/api/assets")
@AllArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class AssetsController {

  @Autowired
  private AssetApplicationService service;

  @Autowired
  private AbstractStructuredLogger apLog;

  /**
   * アセットを取得します。
   * 
   * @param assetCode アセットコード。
   * @return アセット。
   */
  @Operation(summary = "アセットを取得します。", description = "与えられたアセットコードに対応するアセットを返却します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功。",
          content = @Content(mediaType = "image/*",
              schema = @Schema(implementation = Resource.class))),
      @ApiResponse(responseCode = "401", description = "未認証。", content = @Content),
      @ApiResponse(responseCode = "404", description = "アセットコードに対応するアセットがありません。",
          content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー。", content = @Content)})
  @GetMapping("{assetCode}")
  public ResponseEntity<Resource> get(
      @Parameter(required = true,
          description = "アセットコード") @PathVariable("assetCode") String assetCode)
      throws LogicException {
    try {
      AssetResourceInfo assetResourceInfo = this.service.getAssetResourceInfo(assetCode);
      MediaType contentType = getContentType(assetResourceInfo.getAsset());

      return ResponseEntity.ok().contentType(contentType).body(assetResourceInfo.getResource());
    } catch (AssetNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * アセットタイプから Content-Type に変換します。
   * 
   * @param asset アセット。
   * @return Content-Type の名称。
   */
  private MediaType getContentType(Asset asset) {
    switch (asset.getAssetType()) {
      case AssetTypes.PNG:
        return MediaType.IMAGE_PNG;
      default:
        throw new IllegalArgumentException("指定したアセットのアセットタイプは Content-Type に変換できません。");
    }
  }
}
