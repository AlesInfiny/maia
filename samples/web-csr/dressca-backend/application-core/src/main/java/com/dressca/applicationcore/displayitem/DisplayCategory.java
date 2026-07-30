package com.dressca.applicationcore.displayitem;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列カテゴリのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayCategory {
  private UUID id;
  @NonNull
  private String name;

  /**
   * 陳列カテゴリ名を指定して、 {@link DisplayCategory} クラスのインスタンスを初期化します。
   * 
   * @param name 陳列カテゴリ名。
   */
  public DisplayCategory(@NonNull String name) {
    this.name = name;
  }
}
