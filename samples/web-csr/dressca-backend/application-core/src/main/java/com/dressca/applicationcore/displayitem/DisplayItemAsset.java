package com.dressca.applicationcore.displayitem;

import lombok.Data;

/**
 * 掲載品アセットのエンティティです。
 */
@Data
public class DisplayItemAsset {
  private long id;
  private long displayItemId;
  private String assetCode;
}
