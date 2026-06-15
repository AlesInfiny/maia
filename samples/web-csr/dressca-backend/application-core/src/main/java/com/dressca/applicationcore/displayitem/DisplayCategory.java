package com.dressca.applicationcore.displayitem;

import lombok.Data;
import lombok.NonNull;

/**
 * 掲載カテゴリのエンティティです。
 */
@Data
public class DisplayCategory {
  private long id;
  @NonNull
  private String name;
}
