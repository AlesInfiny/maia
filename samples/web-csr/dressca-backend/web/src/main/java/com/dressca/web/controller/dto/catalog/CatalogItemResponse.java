package com.dressca.web.controller.dto.catalog;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemResponse extends CatalogItemSummaryResponse {
  private String description;
  private BigDecimal price;
  private long catalogCategoryId;
  private long catalogBrandId;

  /**
   * コンストラクタ。
   * 
   * @param id ID
   * @param name 名前
   * @param productCode プロダクトコード
   * @param assetCodes アセットコード
   * @param description 商品説明
   * @param price 価格
   * @param catalogCategoryId カタログカテゴリーID
   * @param catalogBrandId カタログブランドID
   */
  public CatalogItemResponse(long id, String name, String productCode, List<String> assetCodes,
      String description, BigDecimal price, long catalogCategoryId, long catalogBrandId) {

    super(id, name, productCode, assetCodes);
    this.description = description;
    this.price = price;
    this.catalogBrandId = catalogCategoryId;
    this.catalogBrandId = catalogBrandId;
  }
}
