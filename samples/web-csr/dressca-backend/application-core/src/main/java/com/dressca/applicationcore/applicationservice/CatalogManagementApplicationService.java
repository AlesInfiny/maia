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
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
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

    private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

    /**
     * 指定したIdのカタログアイテムを取得します。
     * 
     * @param id カタログアイテムID
     * @return 条件に一致するカタログアイテム。存在しない場合は空のOptional。
     */
    public CatalogItem GetCatalogItem(long id) throws CatalogNotFoundException {
        return this.catalogRepository.findById(id).orElseThrow(() -> new CatalogNotFoundException(id));
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
     * @return 追加したカタログアイテム
     */
    public CatalogItem addItemToCatalog(
            String name,
            String description,
            BigDecimal price,
            String productCode,
            long catalogBrandId,
            long catalogCategoryId) {

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
     * @throws CatalogNotFoundException 削除対象のカタログアイテムが存在しなかった場合。
     */
    public void deleteItemFromCatalog(long id) throws CatalogNotFoundException {
        CatalogItem item = this.catalogRepository.findById(id).orElseThrow(() -> new CatalogNotFoundException(id));
        this.catalogRepository.remove(item);
    }

    /**
     * カタログアイテムを更新します。
     * 
     * @param command 更新処理のファサードとなるコマンドオブジェクト。
     * @throws CatalogNotFoundException 更新対象のカタログアイテムが存在しなかった場合。
     */
    public void updateCatalogItem(CatalogItemUpdateCommand command) throws CatalogNotFoundException {

        long id = command.getId();

        this.catalogRepository.findById(id).orElseThrow(() -> new CatalogNotFoundException(id));
        this.catalogBrandRepository.findById(id).orElseThrow();
        this.catalogCategoryRepository.findById(id).orElseThrow();

        CatalogItem item = new CatalogItem(
                id,
                command.getName(),
                command.getDescription(),
                command.getPrice(),
                command.getProductCode(),
                command.getCatalogBrandId(),
                command.getCatalogCategoryId(),
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
