package com.dressca.applicationcore.applicationservice;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
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
    return this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  /**
   * フィルタリング用のカタログブランドリストを取得します。
   * 
   * @return カタログブランドのリスト。
   */
  public List<CatalogBrand> getBrands() {
    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用のカタログカテゴリリストを取得します。
   * 
   * @return カタログカテゴリのリスト。
   */
  public List<CatalogCategory> getCategories() {
    return this.catalogCategoryRepository.getAll();
  }
}