package com.dressca.web.admin.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.applicationservice.CatalogManagementApplicationService;
import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemUpdateCommand;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.OptimisticLockingFailureException;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.admin.controller.dto.catalog.CatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PagedListOfCatalogItemResponse;
import com.dressca.web.admin.controller.dto.catalog.PostCatalogItemRequest;
import com.dressca.web.admin.controller.dto.catalog.PutCatalogItemRequest;
import com.dressca.web.admin.mapper.CatalogItemMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * {@link CatalogItem} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(name = "CatalogItems", description = "カタログアイテムの情報にアクセスする API コントローラーです.")
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class CatalogItemsController {

  @Autowired
  private CatalogManagementApplicationService service;

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 指定したIDのカタログアイテムを返します。
   * 
   * @param id ID。
   * @return カタログアイテム。
   */
  @Operation(summary = "指定したIDのカタログアイテムを返します。", description = "指定したIDのカタログアイテムを返します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedListOfCatalogItemResponse.class))),
      @ApiResponse(responseCode = "404", description = "", content = @Content)
  })
  @GetMapping("{id}")
  public ResponseEntity<CatalogItemResponse> getById(@PathVariable("id") long id) {
    CatalogItem item;
    try {
      item = this.service.getCatalogItem(id);
    } catch (CatalogNotFoundException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
    CatalogItemResponse returnValue = CatalogItemMapper.convert(item);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログアイテムを検索して返します。
   *
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ番号。未指定の場合は1。
   * @param pageSize   ページサイズ。未指定の場合は20。
   * @return カタログアイテムの一覧
   */
  @Operation(summary = "カタログアイテムを検索して返します.", description = "カタログアイテムを検索して返します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedListOfCatalogItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "リクエストエラー", content = @Content)
  })
  @GetMapping
  public ResponseEntity<PagedListOfCatalogItemResponse> getByQuery(
      @RequestParam(name = "brandId", defaultValue = "0") long brandId,
      @RequestParam(name = "categoryId", defaultValue = "0") long categoryId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {

    List<CatalogItemResponse> items = this.service.getCatalogItems(brandId, categoryId, page, pageSize).stream()
        .map(CatalogItemMapper::convert)
        .collect(Collectors.toList());
    int totalCount = this.service.countCatalogItems(brandId, categoryId);

    PagedListOfCatalogItemResponse returnValue = new PagedListOfCatalogItemResponse(
        items,
        totalCount,
        page,
        pageSize);
    return ResponseEntity.ok().body(returnValue);
  }

  /**
   * カタログにアイテムを追加します。
   * 
   * @param postCatalogItemRequest 追加するカタログアイテム
   * @return 追加したカタログアイテム
   */
  @Operation(summary = "カタログにアイテムを追加します。", description = "カタログにアイテムを追加します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "成功。", content = @Content),
      @ApiResponse(responseCode = "401", description = "", content = @Content)
  })
  @PostMapping
  @PreAuthorize(value = "hasRole('ADMIN')")
  public ResponseEntity<CatalogItem> postCatalogItem(@RequestBody PostCatalogItemRequest postCatalogItemRequest) {
    try {
      this.service.addItemToCatalog(
          postCatalogItemRequest.getName(),
          postCatalogItemRequest.getDescription(),
          new BigDecimal(postCatalogItemRequest.getPrice()),
          postCatalogItemRequest.getProductCode(),
          postCatalogItemRequest.getCatalogCategoryId(),
          postCatalogItemRequest.getCatalogBrandId());
    } catch (PermissionDeniedException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.created(URI.create("catalog-items")).build();
  }

  /**
   * カタログから指定したカタログアイテム ID のアイテムを削除します。
   * 
   * @param catalogItemId カタログアイテムID。
   * @return なし。
   */
  @Operation(summary = "カタログから指定したカタログアイテム ID のアイテムを削除します。", description = "カタログから指定したカタログアイテム ID のアイテムを削除します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "成功.", content = @Content),
      @ApiResponse(responseCode = "401", description = "", content = @Content),
      @ApiResponse(responseCode = "404", description = "", content = @Content)
  })

  @DeleteMapping("{catalogItemId}")
  @PreAuthorize(value = "hasRole('ADMIN')")
  public ResponseEntity<CatalogItem> deleteCatalogItem(@PathVariable("catalogItemId") long catalogItemId) {
    try {
      this.service.deleteItemFromCatalog(catalogItemId);
    } catch (PermissionDeniedException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    } catch (CatalogNotFoundException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定したIDのカタログアイテムの情報を更新します。
   * 
   * @param catalogItemId         カタログアイテムID。
   * @param putCatalogItemRequest 更新するカタログアイテムの情報。
   * @return なし。
   */
  @Operation(summary = "指定したIDのカタログアイテムの情報を更新します。", description = "指定したIDのカタログアイテムの情報を更新します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "成功.", content = @Content),
      @ApiResponse(responseCode = "401", description = "認可エラー", content = @Content),
      @ApiResponse(responseCode = "404", description = "対象のIDが存在しない。", content = @Content),
      @ApiResponse(responseCode = "409", description = "更新の競合が発生。", content = @Content),
  })
  @PutMapping("{catalogItemId}")
  @PreAuthorize(value = "hasRole('ADMIN')")
  public ResponseEntity<CatalogItem> putCatalogItem(@PathVariable("catalogItemId") long catalogItemId,
      @RequestBody PutCatalogItemRequest putCatalogItemRequest) {
    CatalogItemUpdateCommand command = new CatalogItemUpdateCommand(
        catalogItemId,
        putCatalogItemRequest.getName(),
        putCatalogItemRequest.getDescription(),
        new BigDecimal(putCatalogItemRequest.getPrice()),
        putCatalogItemRequest.getProductCode(),
        putCatalogItemRequest.getCatalogCategoryId(),
        putCatalogItemRequest.getCatalogBrandId());

    try {
      this.service.updateCatalogItem(command);
    } catch (PermissionDeniedException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    } catch (CatalogNotFoundException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    } catch (CatalogBrandNotFoundException | CatalogCategoryNotFoundException e) {
      apLog.debug(ExceptionUtils.getStackTrace(e));
      // ここでは発生を想定していないので、システムエラーとする。
      throw new SystemException(e, ExceptionIdConstant.E_SHARE0000, null, null);
    } catch (OptimisticLockingFailureException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
    return ResponseEntity.noContent().build();
  }
}