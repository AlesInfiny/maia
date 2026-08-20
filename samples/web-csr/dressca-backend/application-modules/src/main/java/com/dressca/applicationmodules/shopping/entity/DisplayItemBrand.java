package com.dressca.applicationmodules.shopping.entity;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列品ブランドのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayItemBrand {
  private UUID id;
  @NonNull
  private String name;

  /**
   * 陳列品ブランド名を指定して、 {@link DisplayItemBrand} クラスのインスタンスを初期化します。
   * 
   * @param name 陳列品ブランド名。
   */
  public DisplayItemBrand(@NonNull String name) {
    this.name = name;
  }
}
