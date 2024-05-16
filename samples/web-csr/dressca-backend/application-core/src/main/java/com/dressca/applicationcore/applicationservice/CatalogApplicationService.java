package com.dressca.applicationcore.applicationservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.constant.MessageIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.util.MessageUtil;

import lombok.AllArgsConstructor;

/**
 * カタログ情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CatalogApplicationService {
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
    try {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getCatalogItems" }));

      return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
    } finally {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getCatalogItems" }));
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
    try {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "countCatalogItems" }));

      return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
    } finally {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "countCatalogItems" }));
    }
  }

  /**
   * フィルタリング用のカタログブランドリストを取得します。
   * 
   * @return カタログブランドのリスト。
   */
  public List<CatalogBrand> getBrands() {
    try {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getBrands" }));

      return this.brandRepository.getAll();
    } finally {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getBrands" }));
    }
  }

  /**
   * フィルタリング用のカタログカテゴリリストを取得します。
   * 
   * @return カタログカテゴリのリスト。
   */
  public List<CatalogCategory> getCategories() {
    try {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getCategories" }));

      return this.catalogCategoryRepository.getAll();
    } finally {
      apLog.debug(MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getCategories" }));
    }
  }
}
