package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.display.DisplayBrand;
import com.dressca.applicationcore.display.DisplayBrandRepository;
import com.dressca.applicationcore.display.DisplayCategory;
import com.dressca.applicationcore.display.DisplayCategoryRepository;
import com.dressca.applicationcore.display.DisplayItem;
import com.dressca.applicationcore.display.DisplayRepository;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 陳列情報に関するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DisplayApplicationService {
  private final MessageSource messages;
  private final DisplayRepository displayRepository;
  private final DisplayBrandRepository brandRepository;
  private final DisplayCategoryRepository categoryRepository;
  private final AbstractStructuredLogger apLog;

  /**
   * 条件に一致する陳列品を取得します。
   *
   * @param brandId ブランド ID 。
   * @param categoryId カテゴリ ID 。
   * @param page ページ。
   * @param pageSize ページサイズ。
   * @return 条件に一致する陳列品情報のリスト。存在しない場合は空のリスト。
   */
  public List<DisplayItem> getDisplayItems(UUID brandId, UUID categoryId, int page, int pageSize) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_DISPLAY_ITEMS,
        new Object[] {brandId, categoryId, page, pageSize}, Locale.getDefault()));

    return this.displayRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
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

    return this.displayRepository.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  /**
   * 陳列品 ID のリストに一致する陳列品を取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 条件に一致する陳列品のリスト。存在しない場合は空のリスト。
   */
  public List<DisplayItem> getDisplayItemsByIds(List<UUID> displayItemIds) {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_DISPLAY_ITEMS_BY_IDS,
        new Object[] {displayItemIds}, Locale.getDefault()));

    return this.displayRepository.findByDisplayItemIdIn(displayItemIds);
  }

  /**
   * フィルタリング用の陳列ブランドのリストを取得します。
   *
   * @return 陳列ブランドのリスト。
   */
  public List<DisplayBrand> getBrands() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_BRANDS, new Object[] {},
        Locale.getDefault()));

    return this.brandRepository.getAll();
  }

  /**
   * フィルタリング用の陳列カテゴリのリストを取得します。
   *
   * @return 陳列カテゴリのリスト。
   */
  public List<DisplayCategory> getCategories() {
    apLog.debug(messages.getMessage(MessageIdConstants.D_DISPLAY_GET_CATEGORIES, new Object[] {},
        Locale.getDefault()));

    return this.categoryRepository.getAll();
  }
}
