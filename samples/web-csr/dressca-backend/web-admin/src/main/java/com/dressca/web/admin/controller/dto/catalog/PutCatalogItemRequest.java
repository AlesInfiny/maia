package com.dressca.web.admin.controller.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutCatalogItemRequest {
    public String name = "";
    public String description = "";
    public long price;
    public String ProductCode;
    public long CatalogCategoryId;
    public long CatalogBrandId;
    public byte[] rowVersion;
}
