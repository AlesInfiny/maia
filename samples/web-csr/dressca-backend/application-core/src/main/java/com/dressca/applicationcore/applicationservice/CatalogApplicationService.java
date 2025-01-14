package com.dressca.applicationcore.applicationservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.catalog.OptimisticLockingFailureException;
import com.dressca.applicationcore.constant.UserRoleConstants;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.systemcommon.constant.SystemPropertyConstants;

/**
 * カタログ情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CatalogApplicationService {

  @Autowired
  private MessageSource messages;

  private CatalogRepository catalogRepository;
  private CatalogBrandRepository brandRepository;
  private CatalogCategoryRepository categoryRepository;
  private CatalogDomainService catalogDomainService;
  private UserStore userStore;

  /**
   * {@link CatalogApplicationService} クラスの新しいインスタンスを初期化します。
   * 
   * @param messages             メッセージ。
   * @param catalogRepository    カタログリポジトリ。
   * @param brandRepository      カタログブランドリポジトリ。
   * @param categoryRepository   カタログカテゴリリポジトリ。
   * @param catalogDomainService カタログドメインサービス。
   */
  public CatalogApplicationService(MessageSource messages, CatalogRepository catalogRepository,
      CatalogBrandRepository brandRepository, CatalogCategoryRepository categoryRepository,
      CatalogDomainService catalogDomainService) {
    this.messages = messages;
    this.catalogRepository = catalogRepository;
    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
    this.catalogDomainService = catalogDomainService;
  }

  /**
   * {@link UserStore} をセットします。
   * 
   * @param userStore ユーザーのセッション情報。
   */
  @Autowired(required = false)
  public void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 指定した ID のカタログアイテムを取得します。
   * 
   * @param id カタログアイテム ID 。
   * @return 条件に一致するカタログアイテム。
   * @throws CatalogNotFoundException  カタログアイテムが見つからなかった場合。
   * @throws PermissionDeniedException 取得権限がない場合。
   */
  public CatalogItem getCatalogItem(long id) throws CatalogNotFoundException, PermissionDeniedException {
    apLog.debug(
        messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEM, new Object[] { id }, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("getCatalogItem");
    }

    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
      throw new CatalogNotFoundException(id);
    }
    return item;
  }

  /**
   * 利用者が条件に一致するカタログ情報を取得します。
   * 
   * @param brandId    ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page       ページ。
   * @param pageSize   ページサイズ。
   * @return 条件に一致するカタログ情報のリスト。存在しない場合は空のリスト。
   */
  public List<CatalogItem> getCatalogItemsForConsumer(long brandId, long categoryId, int page, int pageSize) {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEMS,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));

    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * 管理者が条件に一致するカタログ情報を取得します。
   * 
   * @param brandId    ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page       ページ。
   * @param pageSize   ページサイズ。
   * @return 条件に一致するカタログ情報のリスト。存在しない場合は空のリスト。
   * @throws PermissionDeniedException 取得権限がない場合。
   */
  public List<CatalogItem> getCatalogItemsForAdmin(long brandId, long categoryId, int page, int pageSize)
      throws PermissionDeniedException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEMS,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("getCatalogItemsForAdmin");
    }

    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * カタログにアイテムを追加します。
   * 
   * @param name              商品名。
   * @param description       説明。
   * @param price             単価。
   * @param productCode       プロダクトコード。
   * @param catalogCategoryId カテゴリ ID 。
   * @param catalogBrandId    ブランド ID 。
   * @return 追加したカタログアイテム。
   * @throws PermissionDeniedException        追加権限がない場合。
   * @throws CatalogCategoryNotFoundException 追加対象のカタログカテゴリが存在しなかった場合。
   * @throws CatalogBrandNotFoundException    追加対象のカタログブランドが存在しなかった場合。
   */
  public CatalogItem addItemToCatalog(String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId)
      throws PermissionDeniedException, CatalogCategoryNotFoundException, CatalogBrandNotFoundException {
    apLog.debug(
        messages.getMessage(MessageIdConstants.D_CATALOG_ADD_ITEM_TO_CATALOG, new Object[] {}, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("addItemToCatalog");
    }

    if (!this.catalogDomainService.existCatalogCategory(catalogCategoryId)) {
      throw new CatalogCategoryNotFoundException(catalogCategoryId);
    }

    if (!this.catalogDomainService.existCatalogBrand(catalogBrandId)) {
      throw new CatalogBrandNotFoundException(catalogBrandId);
    }

    CatalogItem item = CatalogItem.createCatalogItemForRegistration(name, description, price, productCode,
        catalogCategoryId, catalogBrandId);
    item.setRowVersion(LocalDateTime.now());
    CatalogItem catalogItemAdded = this.catalogRepository.add(item);
    return catalogItemAdded;
  }

  /**
   * カタログからアイテムを削除します。
   * 
   * @param id         削除対象のカタログアイテムの ID 。
   * @param rowVersion 行バージョン。
   * @throws PermissionDeniedException         削除権限がない場合。
   * @throws CatalogNotFoundException          削除対象のカタログアイテムが存在しなかった場合。
   * @throws OptimisticLockingFailureException 楽観ロックエラーの場合。
   */
  public void deleteItemFromCatalog(long id, LocalDateTime rowVersion)
      throws PermissionDeniedException, CatalogNotFoundException, OptimisticLockingFailureException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_DELETE_ITEM_FROM_CATALOG, new Object[] { id },
        Locale.getDefault()));
    if (!this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("deleteItemFromCatalog");
    }
    if (!this.catalogDomainService.existCatalogItem(id)) {
      throw new CatalogNotFoundException(id);
    }
    int deleteRowCount = this.catalogRepository.remove(id, rowVersion);
    if (deleteRowCount == 0) {
      throw new OptimisticLockingFailureException(id);
    }
  }

  /**
   * カタログアイテムを更新します。
   * 
   * @param id                更新対象のカタログアイテム ID 。
   * @param name              商品名。
   * @param description       説明。
   * @param price             価格。
   * @param productCode       プロダクトコード。
   * @param catalogCategoryId カテゴリ ID 。
   * @param catalogBrandId    ブランド ID 。
   * @param rowVersion        行バージョン。
   * @throws CatalogNotFoundException          更新対象のカタログアイテムが存在しなかった場合。
   * @throws PermissionDeniedException         更新権限がない場合。
   * @throws CatalogCategoryNotFoundException  更新対象のカタログカテゴリが存在しなかった場合。
   * @throws CatalogBrandNotFoundException     更新対象のカタログブランドが存在しなかった場合。
   * @throws OptimisticLockingFailureException 楽観ロックエラーの場合。
   */
  public void updateCatalogItem(long id, String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId, LocalDateTime rowVersion)
      throws CatalogNotFoundException, PermissionDeniedException, CatalogCategoryNotFoundException,
      CatalogBrandNotFoundException, OptimisticLockingFailureException {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_UPDATE_CATALOG_ITEM, new Object[] { id },
        Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("updateCatalogItem");
    }

    if (!this.catalogDomainService.existCatalogItem(id)) {
      throw new CatalogNotFoundException(id);
    }

    if (!this.catalogDomainService.existCatalogCategory(catalogCategoryId)) {
      throw new CatalogCategoryNotFoundException(catalogCategoryId);
    }

    if (!this.catalogDomainService.existCatalogBrand(catalogBrandId)) {
      throw new CatalogBrandNotFoundException(catalogBrandId);
    }

    CatalogItem item = new CatalogItem(id, name, description, price, productCode, catalogCategoryId, catalogBrandId);
    // 変更前の行バージョンを、変更対象のカタログアイテムに追加
    item.setRowVersion(rowVersion);

    int updateRowCount = this.catalogRepository.update(item);
    if (updateRowCount == 0) {
      throw new OptimisticLockingFailureException(id);
    }
  }

  /**
   * 条件に一致するカテゴリの件数を取得します。
   * 
   * @param brandId    ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致するカタログ情報の件数。
   */
  public int countCatalogItems(long brandId, long categoryId) {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_COUNT_CATALOG_ITEMS,
        new Object[] { brandId, categoryId },
        Locale.getDefault()));

    return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  /**
   * フィルタリング用のカタログブランドリストを取得します。
   * 
   * @return カタログブランドのリスト。
   */
  public List<CatalogBrand> getBrands() {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_BRANDS, new Object[] {}, Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用のカタログカテゴリリストを取得します。
   * 
   * @return カタログカテゴリのリスト。
   */
  public List<CatalogCategory> getCategories() {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATEGORIES, new Object[] {}, Locale.getDefault()));

    return this.categoryRepository.getAll();
  }
}
