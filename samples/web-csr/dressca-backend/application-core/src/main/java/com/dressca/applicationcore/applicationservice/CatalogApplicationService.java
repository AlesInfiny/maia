package com.dressca.applicationcore.applicationservice;

import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import lombok.AllArgsConstructor;

/**
 * カタログ情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CatalogApplicationService {

  @Autowired
  private MessageSource messages;

  private CatalogRepository catalogRepository;
  private CatalogBrandRepository brandRepository;
  private CatalogCategoryRepository catalogCategoryRepository;

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

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

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATALOG_ITEMS,
        new Object[] { brandId, categoryId, page, pageSize }, Locale.getDefault()));

    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * 条件に一致するカテゴリの件数を取得します。
   * 
   * @param brandId    ブランドID
   * @param categoryId カテゴリID
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

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_BRANDS,
        new Object[] {},
        Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用のカタログカテゴリリストを取得します。
   * 
   * @return カタログカテゴリのリスト。
   */
  public List<CatalogCategory> getCategories() {

    apLog.debug(messages.getMessage(MessageIdConstants.D_CATALOG_GET_CATEGORIES,
        new Object[] {},
        Locale.getDefault()));

    return this.catalogCategoryRepository.getAll();
  }
}
