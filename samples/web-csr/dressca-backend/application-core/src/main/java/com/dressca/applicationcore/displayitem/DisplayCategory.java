package com.dressca.applicationcore.displayitem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 掲載カテゴリのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayCategory {
  private long id;
  @NonNull
  private String name;
}
