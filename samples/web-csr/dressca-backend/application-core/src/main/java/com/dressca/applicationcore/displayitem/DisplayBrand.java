package com.dressca.applicationcore.displayitem;

import lombok.Data;
import lombok.NonNull;

/**
 * 掲載ブランドのエンティティです。
 */
@Data
public class DisplayBrand {
  private long id;
  @NonNull
  private String name;
}
