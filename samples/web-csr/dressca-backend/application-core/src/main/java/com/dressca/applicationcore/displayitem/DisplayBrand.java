package com.dressca.applicationcore.displayitem;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列ブランドのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayBrand {
  private UUID id;
  @NonNull
  private String name;

  /**
   * 陳列ブランド名を指定して、 {@link DisplayBrand} クラスのインスタンスを初期化します。
   * 
   * @param name 陳列ブランド名。
   */
  public DisplayBrand(@NonNull String name) {
    this.name = name;
  }
}
