package com.dressca.applicationcore.applicationservice;

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
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.constant.UserRoleConstants;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * カタログ情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CatalogApplicationService {
  private final MessageSource messages;
  private final CatalogRepository catalogRepository;
  private final CatalogBrandRepository brandRepository;
  private final CatalogCategoryRepository categoryRepository;
  private final CatalogDomainService catalogDomainService;
  private final AbstractStructuredLogger apLog;
  private final UserStore userStore;

  public CatalogApplicationService(MessageSource messages, CatalogRepository catalogRepository,
      CatalogBrandRepository brandRepository, CatalogCategoryRepository categoryRepository,
      CatalogDomainService catalogDomainService, AbstractStructuredLogger apLog,
      @Autowired(required = false) UserStore userStore) {
    this.messages = messages;
    this.catalogRepository = catalogRepository;
    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
    this.catalogDomainService = catalogDomainService;
    this.apLog = apLog;
    this.userStore = userStore;
  }

  public CatalogItem getCatalogItem(UUID id)
      throws CatalogNotFoundException, PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEM,
        new Object[] {id}, Locale.getDefault()));

    if (userStore == null || !this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("getCatalogItem");
    }

    CatalogItem item = this.catalogRepository.findByIdIncludingDeleted(id);
    if (item == null) {
      throw new CatalogNotFoundException(id);
    }
    return item;
  }

  public List<CatalogItem> getCatalogItemsForConsumer(UUID brandId, UUID categoryId, int page,
      int pageSize) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEMS,
        new Object[] {brandId, categoryId, page, pageSize}, Locale.getDefault()));

    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  public List<CatalogItem> getCatalogItemsForAdmin(UUID brandId, UUID categoryId, int page,
      int pageSize) throws PermissionDeniedException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEMS,
        new Object[] {brandId, categoryId, page, pageSize}, Locale.getDefault()));

    if (userStore == null || !this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("getCatalogItemsForAdmin");
    }

    return this.catalogRepository.findByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId,
        page, pageSize);
  }

  public CatalogItem addItemToCatalog(String name, String description, BigDecimal price,
      String productCode, UUID catalogCategoryId, UUID catalogBrandId)
      throws PermissionDeniedException, CatalogCategoryNotFoundException,
      CatalogBrandNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_ADD_ITEM_TO_CATALOG,
        new Object[] {}, Locale.getDefault()));

    if (userStore == null || !this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException("addItemToCatalog");
    }

    if (!this.catalogDomainService.existCatalogCategory(catalogCategoryId)) {
      throw new CatalogCategoryNotFoundException(catalogCategoryId);
    }

    if (!this.catalogDomainService.existCatalogBrand(catalogBrandId)) {
      throw new CatalogBrandNotFoundException(catalogBrandId);
    }

    CatalogItem item = CatalogItem.createCatalogItemForRegistration(name, description, price,
        productCode, catalogCategoryId, catalogBrandId, false);
    item.setRowVersion(OffsetDateTime.now());
    return this.catalogRepository.add(item);
  }

  public void deleteItemFromCatalog(UUID id, OffsetDateTime rowVersion)
      throws PermissionDeniedException, CatalogNotFoundException,
      OptimisticLockingFailureException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_DELETE_ITEM_FROM_CATALOG,
        new Object[] {id}, Locale.getDefault()));

    final String operationName = "deleteItemFromCatalog";
    if (userStore == null || !this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException(operationName);
    }

    if (!this.catalogDomainService.existCatalogItem(id)) {
      throw new CatalogNotFoundException(id);
    }

    int deleteRowCount = this.catalogRepository.remove(id, rowVersion);
    if (deleteRowCount == 0) {
      throw new OptimisticLockingFailureException(id, operationName);
    }
  }

  public void updateCatalogItem(UUID id, String name, String description, BigDecimal price,
      String productCode, UUID catalogCategoryId, UUID catalogBrandId, OffsetDateTime rowVersion,
      boolean isDeleted)
      throws CatalogNotFoundException, PermissionDeniedException, CatalogCategoryNotFoundException,
      CatalogBrandNotFoundException, OptimisticLockingFailureException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_UPDATE_CATALOG_ITEM,
        new Object[] {id}, Locale.getDefault()));
    final String operationName = "updateCatalogItem";
    if (userStore == null || !this.userStore.isInRole(UserRoleConstants.ADMIN)) {
      throw new PermissionDeniedException(operationName);
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

    CatalogItem item = new CatalogItem(id, name, description, price, productCode,
        catalogCategoryId, catalogBrandId, isDeleted);
    item.setRowVersion(rowVersion);

    int updateRowCount = this.catalogRepository.update(item);
    if (updateRowCount == 0) {
      throw new OptimisticLockingFailureException(id, operationName);
    }
  }

  public int countCatalogItemsForConsumer(UUID brandId, UUID categoryId) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_COUNT_CATALOG_ITEMS,
        new Object[] {brandId, categoryId}, Locale.getDefault()));

    return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  public int countCatalogItemsForAdmin(UUID brandId, UUID categoryId) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_COUNT_CATALOG_ITEMS,
        new Object[] {brandId, categoryId}, Locale.getDefault()));

    return this.catalogRepository.countByBrandIdAndCategoryIdIncludingDeleted(brandId,
        categoryId);
  }

  public List<CatalogBrand> getBrands() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_BRANDS, new Object[] {},
        Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  public List<CatalogCategory> getCategories() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATEGORIES, new Object[] {},
        Locale.getDefault()));

    return this.categoryRepository.getAll();
  }
}
