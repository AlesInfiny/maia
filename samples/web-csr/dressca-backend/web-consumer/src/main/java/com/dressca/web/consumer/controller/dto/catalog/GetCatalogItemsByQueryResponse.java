package com.dressca.web.consumer.controller.dto.catalog;

import com.dressca.web.consumer.controller.dto.catalog.PagedListOfCatalogItemApiModel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログアイテムの検索クエリに対する dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogItemsByQueryResponse {
    @NotNull
    private PagedListOfCatalogItemApiModel catalogItems;
}
