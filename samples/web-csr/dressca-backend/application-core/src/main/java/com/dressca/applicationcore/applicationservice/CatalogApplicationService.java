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
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.constant.MessageIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.constant.UserRoleConstant;
import com.dressca.systemcommon.exception.OptimisticLockingFailureException;

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
  private UserStore userStore;

  /**
   * コンストラクタ。
   * 
   * @param messages           メッセージ
   * @param catalogRepository  カタログリポジトリ。
   * @param brandRepository    カタログブランドリポジトリ。
   * @param categoryRepository カタログカテゴリリポジトリ。
   */
  public CatalogApplicationService(MessageSource messages, CatalogRepository catalogRepository,
      CatalogBrandRepository brandRepository, CatalogCategoryRepository categoryRepository) {
    this.messages = messages;
    this.catalogRepository = catalogRepository;
    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
  }

  @Autowired(required = false)
  public void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 指定したIdのカタログアイテムを取得します。
   * 
   * @param id カタログアイテムID
   * @return 条件に一致するカタログアイテム。
   * @throws CatalogNotFoundException  カタログアイテムが見つからなかった場合。
   * @throws PermissionDeniedException 取得権限がない場合。
   */
  public CatalogItem getCatalogItem(long id) throws CatalogNotFoundException, PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0005_LOG, new Object[] { id }, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstant.ADMIN)) {
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
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ
   * @param pageSize   ページサイズ
   * @return 条件に一致するカタログ情報のリスト。存在しない場合は空のリスト。
   */
  public List<CatalogItem> getCatalogItemsByConsumer(long brandId, long categoryId, int page, int pageSize) {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0001_LOG,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));

    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * 管理者が条件に一致するカタログ情報を取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @param page       ページ
   * @param pageSize   ページサイズ
   * @return 条件に一致するカタログ情報のリスト。存在しない場合は空のリスト。
   * @throws PermissionDeniedException 取得権限がない場合。
   */
  public List<CatalogItem> getCatalogItemsByAdmin(long brandId, long categoryId, int page, int pageSize)
      throws PermissionDeniedException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0001_LOG,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstant.ADMIN)) {
      throw new PermissionDeniedException("getCatalogItemsByAdmin");
    }

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
  public CatalogItem addItemToCatalog(String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId) throws PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0006_LOG, new Object[] {}, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstant.ADMIN)) {
      throw new PermissionDeniedException("addItemToCatalog");
    }

    // 0は仮の値で、DBにINSERTされる時にDBによって自動採番される
    CatalogItem item = new CatalogItem(0, name, description, price, productCode, catalogCategoryId, catalogBrandId);
    item.setRowVersion(LocalDateTime.now());
    CatalogItem catalogItemAdded = this.catalogRepository.add(item);
    return catalogItemAdded;
  }

  /**
   * カタログからアイテムを削除します。
   * 
   * @param id         削除対象のカタログアイテムのID。
   * @param rowVersion 行バージョン。
   * @throws PermissionDeniedException         削除権限がない場合。
   * @throws CatalogNotFoundException          削除対象のカタログアイテムが存在しなかった場合。
   * @throws OptimisticLockingFailureException 楽観ロックエラーの場合。
   * 
   */
  public void deleteItemFromCatalog(long id, LocalDateTime rowVersion)
      throws CatalogNotFoundException, PermissionDeniedException, OptimisticLockingFailureException {
    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0007_LOG, new Object[] { id }, Locale.getDefault()));
    if (!this.userStore.isInRole(UserRoleConstant.ADMIN)) {
      throw new PermissionDeniedException("deleteItemFromCatalog");
    }
    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
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
   * @param id                更新対象のカタログアイテムID。
   * @param name              商品名。
   * @param description       説明。
   * @param price             価格。
   * @param productCode       商品コード。
   * @param catalogCategoryId カテゴリID。
   * @param catalogBrandId    ブランドID。
   * @param rowVersion        行バージョン。
   * @throws CatalogNotFoundException          更新対象のカタログアイテムが存在しなかった場合。
   * @throws PermissionDeniedException         更新権限がない場合。
   * @throws CatalogBrandNotFoundException     更新対象のカタログブランドが存在しなかった場合。
   * @throws CatalogCategoryNotFoundException  更新対象のカタログカテゴリが存在しなかった場合。
   * @throws OptimisticLockingFailureException 楽観ロックエラーの場合。
   */
  public void updateCatalogItem(long id, String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId, LocalDateTime rowVersion)
      throws CatalogNotFoundException, PermissionDeniedException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0008_LOG, new Object[] { id }, Locale.getDefault()));

    if (!this.userStore.isInRole(UserRoleConstant.ADMIN)) {
      throw new PermissionDeniedException("updateCatalogItem");
    }
    CatalogItem currentCatalogItem = catalogRepository.findById(id);
    if (currentCatalogItem == null) {
      throw new CatalogNotFoundException(id);
    }

    CatalogCategory catalogCategory = categoryRepository.findById(catalogCategoryId);
    if (catalogCategory == null) {
      throw new CatalogCategoryNotFoundException(catalogCategoryId);
    }

    CatalogBrand catalogBrand = brandRepository.findById(catalogBrandId);
    if (catalogBrand == null) {
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
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
   * @return 条件に一致するカタログ情報の件数。
   */
  public int countCatalogItems(long brandId, long categoryId) {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0002_LOG, new Object[] { brandId, categoryId },
        Locale.getDefault()));

    return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  /**
   * フィルタリング用のカタログブランドリストを取得します。
   * 
   * @return カタログブランドのリスト。
   */
  public List<CatalogBrand> getBrands() {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0003_LOG, new Object[] {}, Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用のカタログカテゴリリストを取得します。
   * 
   * @return カタログカテゴリのリスト。
   */
  public List<CatalogCategory> getCategories() {

    apLog.debug(messages.getMessage(MessageIdConstant.D_CATALOG0004_LOG, new Object[] {}, Locale.getDefault()));

    return this.categoryRepository.getAll();
  }
}
