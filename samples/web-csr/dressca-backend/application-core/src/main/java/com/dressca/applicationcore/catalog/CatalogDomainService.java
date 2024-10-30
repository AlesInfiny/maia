package com.dressca.applicationcore.catalog;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.dressca.systemcommon.exception.OptimisticLockingFailureException;
import lombok.AllArgsConstructor;

/**
 * カタログに関するドメインサービスを提供します。
 */
@Service
@AllArgsConstructor
public class CatalogDomainService {
  private CatalogRepository catalogRepository;
  private CatalogCategoryRepository catalogCategoryRepository;
  private CatalogBrandRepository catalogBrandRepository;

  /**
   * 指定したカタログアイテム Id のうち、存在するカタログアイテムの一覧を返却します。
   * 
   * @param catalogItemIds カタログアイテム Id のリスト
   * @return 存在するカタログアイテムの一覧
   */
  public List<CatalogItem> getExistCatalogItems(List<Long> catalogItemIds) {
    return this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
  }

  /**
   * 指定したカタログアイテム Id がリポジトリ内にすべて存在するかを取得します。
   * 
   * @param catalogItemIds カタログアイテム Id のリスト
   * @return すべて存在する場合は true、一部でも不在の場合は false。
   */
  public boolean existAll(List<Long> catalogItemIds) {
    List<CatalogItem> items = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
    List<Long> notExistCatalogItemIds = catalogItemIds.stream()
        .filter(catalogItemId -> !this.existCatalogItemIdInItems(items, catalogItemId))
        .collect(Collectors.toList());

    return notExistCatalogItemIds.isEmpty();
  }

  /**
   * 指定したカタログアイテム Id のカタログアイテムを取得します。
   * 
   * @param id カタログアイテム Id
   * @return カタログアイテム
   * @throws CatalogNotFoundException 取得対象のカタログアイテムがない場合。
   */
  public CatalogItem getCatalogItemById(long id) throws CatalogNotFoundException {
    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
      throw new CatalogNotFoundException(id);
    }
    return item;
  }

  /**
   * 指定した条件のカタログアイテムを取得します。
   * 
   * @param brandId    カタログブランドId。
   * @param categoryId カタログカテゴリId。
   * @param page       ページ。
   * @param pageSize   ページサイズ。
   * @return 条件に合うカタログアイテムのリスト。
   */
  public List<CatalogItem> getCatalogItemsByConditions(long brandId, long categoryId, int page, int pageSize) {
    return this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize);
  }

  /**
   * カタログアイテムを追加します。
   * 
   * @param name              商品名。
   * @param description       説明。
   * @param price             価格。
   * @param productCode       商品コード。
   * @param catalogCategoryId カタログカテゴリId。
   * @param catalogBrandId    カタログブランドId。
   * @return 追加したカタログアイテム。
   */
  public CatalogItem addCatalogItem(String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId) {
    // 0は仮の値で、DBにINSERTされる時にDBによって自動採番される
    CatalogItem item = new CatalogItem(0, name, description, price, productCode, catalogCategoryId, catalogBrandId);
    item.setRowVersion(1);
    CatalogItem catalogItemAdded = this.catalogRepository.add(item);
    return catalogItemAdded;
  }

  /**
   * 指定したカタログアイテムを削除します。
   * 
   * @param id 削除するカタログアイテムId。
   * @throws CatalogNotFoundException 削除対象のカタログアイテムがない場合。
   */
  public void deleteCatalogItemById(long id) throws CatalogNotFoundException {
    CatalogItem item = this.catalogRepository.findById(id);
    if (item == null) {
      throw new CatalogNotFoundException(id);
    }
    this.catalogRepository.remove(item);
  }

  /**
   * カタログアイテムの情報を変更します。
   * 
   * @param id                カタログアイテム id。
   * @param name              商品名。
   * @param description       説明。
   * @param price             価格。
   * @param productCode       商品コード。
   * @param catalogCategoryId カタログカテゴリId。
   * @param catalogBrandId    カタログブランドId。
   * @throws CatalogNotFoundException          更新対象のカタログアイテムがない場合。
   * @throws CatalogBrandNotFoundException     更新対象のカタログブランドがない場合。
   * @throws CatalogCategoryNotFoundException  更新対象のカタログカテゴリがない場合。
   * @throws OptimisticLockingFailureException 楽観ロックエラー。
   */
  public void modifyCatalogItem(long id, String name, String description, BigDecimal price, String productCode,
      long catalogCategoryId, long catalogBrandId)
      throws CatalogNotFoundException, CatalogBrandNotFoundException, CatalogCategoryNotFoundException,
      OptimisticLockingFailureException {

    CatalogItem currentCatalogItem = catalogRepository.findById(id);
    if (currentCatalogItem == null) {
      throw new CatalogNotFoundException(id);
    }

    CatalogCategory catalogCategory = catalogCategoryRepository.findById(catalogCategoryId);
    if (catalogCategory == null) {
      throw new CatalogCategoryNotFoundException(catalogCategoryId);
    }

    CatalogBrand catalogBrand = catalogBrandRepository.findById(catalogBrandId);
    if (catalogBrand == null) {
      throw new CatalogBrandNotFoundException(catalogBrandId);
    }

    CatalogItem item = new CatalogItem(id, name, description, price, productCode, catalogCategoryId, catalogBrandId);
    // 変更前の行バージョンを取得し、変更対象のカタログアイテムに追加
    item.setRowVersion(currentCatalogItem.getRowVersion());

    int updateRowCount = this.catalogRepository.update(item);
    if (updateRowCount == 0) {
      throw new OptimisticLockingFailureException(id);
    }
  }

  private boolean existCatalogItemIdInItems(List<CatalogItem> items, long catalogItemId) {
    return items.stream()
        .anyMatch(catalogItem -> catalogItem.getId() == catalogItemId);
  }
}
