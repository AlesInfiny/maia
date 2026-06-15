package com.dressca.applicationcore.displayitem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

/**
 * 掲載品のエンティティです。
 */
@Data
public class DisplayItem {
  private Long id;
  private Long catalogItemId;
  private List<DisplayItemAsset> assets = new ArrayList<>();
  @NonNull
  private String name;
  @NonNull
  private String description;
  @NonNull
  private BigDecimal price;
  @NonNull
  private String productCode;
  private long displayCategoryId;
  private long displayBrandId;
  private boolean isDeleted;
}
