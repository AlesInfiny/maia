package com.dressca.applicationcore.applicationservice;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.displayitem.DisplayBrand;
import com.dressca.applicationcore.displayitem.DisplayBrandRepository;
import com.dressca.applicationcore.displayitem.DisplayCategory;
import com.dressca.applicationcore.displayitem.DisplayCategoryRepository;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayRepository;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import lombok.RequiredArgsConstructor;

/**
 * 掲載情報に関するアプリケーションサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DisplayApplicationService {
  private final AbstractStructuredLogger apLog;
  private final MessageSource messages;
  private final DisplayBrandRepository brandRepository;
  private final DisplayCategoryRepository categoryRepository;
  private final DisplayRepository displayRepository;

  /**
   * 条件に一致する掲載品のリストを取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致する掲載品のリスト。存在しない場合は空のリスト。
   */
  public List<DisplayItem> getDisplayItems(long brandId, long categoryId, int page, int pageSize) {

    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_DISPLAY_ITEMS,
        new Object[] {brandId, categoryId, page, pageSize}, Locale.getDefault()));

    return this.displayRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * フィルタリング用の掲載ブランドリストを取得します。
   * 
   * @return 掲載ブランドのリスト。
   */
  public List<DisplayBrand> getBrands() {

    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_BRANDS, new Object[] {},
        Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用の掲載カテゴリリストを取得します。
   * 
   * @return 掲載カテゴリのリスト。
   */
  public List<DisplayCategory> getCategories() {

    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_CATEGORIES, new Object[] {},
        Locale.getDefault()));

    return this.categoryRepository.getAll();
  }

  /**
   * 条件に一致する掲載品の件数を取得します。
   * 
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致する掲載品の件数。
   */
  public int countDisplayItems(long brandId, long categoryId) {

    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_COUNT_DISPLAY_ITEMS,
        new Object[] {brandId, categoryId}, Locale.getDefault()));

    return this.displayRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

}
