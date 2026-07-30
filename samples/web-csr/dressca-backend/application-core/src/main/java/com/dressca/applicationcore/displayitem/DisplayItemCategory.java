package com.dressca.applicationcore.displayitem;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列品カテゴリのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayItemCategory {
  private UUID id;
  @NonNull
  private String name;

  /**
   * 陳列品カテゴリ名を指定して、 {@link DisplayItemCategory} クラスのインスタンスを初期化します。
   * 
   * @param name 陳列品カテゴリ名。
   */
  public DisplayItemCategory(@NonNull String name) {
    this.name = name;
  }
}
