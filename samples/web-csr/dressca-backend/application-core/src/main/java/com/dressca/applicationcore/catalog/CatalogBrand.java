package com.dressca.applicationcore.catalog;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログブランドのドメインモデル。
 * カタログアイテムの製造元や企画元に基づいて定義されるブランドを表現します。
 */
@Data
public class CatalogBrand {
  @NonNull
  private String name;
  private List<CatalogItem> items = new ArrayList<>();
}
