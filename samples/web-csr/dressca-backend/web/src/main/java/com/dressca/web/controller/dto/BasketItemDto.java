package com.dressca.web.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDto {
    private long catalogItemId;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subTotal;
    private CatalogItemSummaryDto catalogItem;
}
