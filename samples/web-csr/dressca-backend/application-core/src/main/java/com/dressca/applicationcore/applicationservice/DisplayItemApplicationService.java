package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayItemBrand;
import com.dressca.applicationcore.displayitem.DisplayItemBrandRepository;
import com.dressca.applicationcore.displayitem.DisplayItemCategory;
import com.dressca.applicationcore.displayitem.DisplayItemCategoryRepository;
import com.dressca.applicationcore.displayitem.DisplayItemRepository;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 陳列品に関するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DisplayItemApplicationService {
  private final MessageSource messages;
  private final DisplayItemRepository displayItemRepository;
  private final DisplayItemBrandRepository displayItemBrandRepository;
  private final DisplayItemCategoryRepository displayItemCategoryRepository;
  private final AbstractStructuredLogger apLog;

  /**
   * 条件に一致する陳列品を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致する陳列品のリスト。存在しない場合は空のリスト。
   */
  public List<DisplayItem> getDisplayItems(UUID brandId, UUID categoryId, int page, int pageSize) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_DISPLAY_ITEMS,
        new Object[] {brandId, categoryId, page, pageSize}, Locale.getDefault()));

    return this.displayItemRepository.findByBrandIdAndCategoryId(brandId, categoryId, page,
        pageSize);
  }

  /**
   * 条件に一致する陳列品の件数を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @return 条件に一致する陳列品の件数。
   */
  public int countDisplayItems(UUID brandId, UUID categoryId) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_COUNT_DISPLAY_ITEMS,
        new Object[] {brandId, categoryId}, Locale.getDefault()));

    return this.displayItemRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  /**
   * フィルタリング用の陳列品ブランドのリストを取得します。
   *
   * @return 陳列品ブランドのリスト。
   */
  public List<DisplayItemBrand> getBrands() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_BRANDS, new Object[] {},
        Locale.getDefault()));

    return this.displayItemBrandRepository.getAll();
  }

  /**
   * フィルタリング用の陳列品カテゴリのリストを取得します。
   *
   * @return 陳列品カテゴリのリスト。
   */
  public List<DisplayItemCategory> getCategories() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_CATEGORIES, new Object[] {},
        Locale.getDefault()));

    return this.displayItemCategoryRepository.getAll();
  }
}
