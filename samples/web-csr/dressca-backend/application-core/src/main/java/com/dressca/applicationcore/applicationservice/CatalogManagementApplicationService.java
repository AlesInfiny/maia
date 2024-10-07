package com.dressca.applicationcore.applicationservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemUpdateCommand;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.constant.MessageIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import lombok.AllArgsConstructor;

/**
 * カタログ管理に関するビジネスユースケースを実現するアプリケーションサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CatalogManagementApplicationService {

  @Autowired
  private MessageSource messages;

  private CatalogRepository catalogRepository;
  private CatalogBrandRepository catalogBrandRepository;
  private CatalogCategoryRepository catalogCategoryRepository;

  @Autowired
  private UserStore userStore;

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 指定したIdのカタログアイテムを取得します。
   * 
   * @param id カタログアイテムID
   * @return 条件に一致するカタログアイテム。
   * @throws CatalogNotFoundException カタログアイテムが見つからなかった場合。
   */
  public CatalogItem getCatalogItem(long id) throws CatalogNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0005_LOG,
        new Object[] { id }, Locale.getDefault()));
    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
      CatalogNotFoundException e = new CatalogNotFoundException(id);
      apLog.info(e.getMessage());
      throw e;
    }
    return item;
  }

  /**
   * 条件に一致するカタログ情報を取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ
   * @param pageSize   ページサイズ
   * @return 条件に一致するカタログ情報のリスト。存在しない場合は空のリスト。
   */
  public List<CatalogItem> getCatalogItems(long brandId, long categoryId, int page, int pageSize) {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0001_LOG,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));
    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * カタログにアイテムを追加します。
   * 
   * @param name              商品名
   * @param description       説明
   * @param price             単価
   * @param productCode       商品コード
   * @param catalogBrandId    ブランドID
   * @param catalogCategoryId カテゴリID
   * @return 追加したカタログアイテム。
   * @throws PermissionDeniedException 追加権限がない場合。
   */
  public CatalogItem addItemToCatalog(
      String name,
      String description,
      BigDecimal price,
      String productCode,
      long catalogBrandId,
      long catalogCategoryId) throws PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0006_LOG,
        new Object[] {}, Locale.getDefault()));
    if (!this.userStore.isInRole("ROLE_ADMIN")) {
      throw new PermissionDeniedException("addItemToCatalog");
    }
    CatalogItem item = new CatalogItem(name,
        description,
        price,
        productCode,
        catalogBrandId,
        catalogCategoryId);
    CatalogItem catalogItemAdded = this.catalogRepository.add(item);
    return catalogItemAdded;
  }

  /**
   * カタログからアイテムを削除します。
   * 
   * @param id 削除対象のカタログアイテムのID。
   * @throws PermissionDeniedException 削除権限がない場合。
   * @throws CatalogNotFoundException  削除対象のカタログアイテムが存在しなかった場合。
   * 
   */
  public void deleteItemFromCatalog(long id) throws CatalogNotFoundException, PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0007_LOG,
        new Object[] { id }, Locale.getDefault()));
    if (!this.userStore.isInRole("ROLE_ADMIN")) {
      throw new PermissionDeniedException("deleteItemFromCatalog");
    }
    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
      CatalogNotFoundException e = new CatalogNotFoundException(id);
      apLog.info(e.getMessage());
      throw e;
    }
    this.catalogRepository.remove(item);
  }

  /**
   * カタログアイテムを更新します。
   * 
   * @param command 更新処理のファサードとなるコマンドオブジェクト。
   * @throws PermissionDeniedException        更新権限がない場合。
   * @throws CatalogNotFoundException         更新対象のカタログアイテムが存在しなかった場合。
   * @throws CatalogBrandNotFoundException    更新対象のカタログブランドが存在しなかった場合。
   * @throws CatalogCategoryNotFoundException 更新対象のカタログカテゴリが存在しなかった場合。
   */
  public void updateCatalogItem(CatalogItemUpdateCommand command)
      throws CatalogNotFoundException, PermissionDeniedException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0008_LOG,
        new Object[] { command.getId() }, Locale.getDefault()));

    if (!this.userStore.isInRole("ROLE_ADMIN")) {
      throw new PermissionDeniedException("updateCatalogItem");
    }

    long catalogItemId = command.getId();
    if (catalogRepository.findById(catalogItemId) == null) {
      CatalogNotFoundException e = new CatalogNotFoundException(catalogItemId);
      apLog.info(e.getMessage());
      throw e;
    }

    long catalogBrandId = command.getCatalogBrandId();
    CatalogBrand catalogBrand = catalogBrandRepository.findById(catalogBrandId);
    if (catalogBrand == null) {
      CatalogBrandNotFoundException e = new CatalogBrandNotFoundException(catalogBrandId);
      apLog.info(e.getMessage());
      throw e;
    }

    long catalogCategoryId = command.getCatalogCategoryId();
    CatalogCategory catalogCategory = catalogCategoryRepository.findById(catalogCategoryId);
    if (catalogCategory == null) {
      CatalogCategoryNotFoundException e = new CatalogCategoryNotFoundException(catalogCategoryId);
      apLog.info(e.getMessage());
      throw e;
    }

    CatalogItem item = new CatalogItem(
        catalogItemId,
        command.getName(),
        command.getDescription(),
        command.getPrice(),
        command.getProductCode(),
        catalogBrandId,
        catalogCategoryId,
        command.getRowVersion());

    this.catalogRepository.update(item);
  }

  /**
   * 条件に一致するカテゴリの件数を取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @return 条件に一致するカタログ情報の件数。
   */
  public int countCatalogItems(long brandId, long categoryId) {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0002_LOG, new Object[] { brandId, categoryId },
        Locale.getDefault()));
    return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }
}