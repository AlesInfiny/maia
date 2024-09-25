package com.dressca.applicationcore.catalog;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CatalogItemUpdateCommand {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String productCode;
    private long catalogBrandId;
    private long catalogCategoryId;
    private byte[] rowVersion;
}
