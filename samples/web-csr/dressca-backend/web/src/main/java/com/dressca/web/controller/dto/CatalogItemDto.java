package com.dressca.web.controller.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CatalogItemDto extends CatalogItemSummaryDto {
    private String description;
    private BigDecimal price;
    private long catalogCategoryId;
    private long catalogBrandId;

    public CatalogItemDto(long id, String name, String productCode, List<String> assetCodes,
        String description, BigDecimal price, long catalogCategoryId, long catalogBrandId) {

        super(id, name, productCode, assetCodes);
        this.description = description;
        this.price = price;
        this.catalogBrandId = catalogCategoryId;
        this.catalogBrandId = catalogBrandId;
    }
}
