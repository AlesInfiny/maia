package com.dressca.applicationcore.displayitem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 掲載ブランドのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayBrand {
  private long id;
  @NonNull
  private String name;
}
