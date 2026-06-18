package com.dressca.applicationcore.catalog;

import java.util.Arrays;
import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.exception.LogicException;

/**
 * 商品が存在しないことを表す例外です。
 */
public class CatalogNotFoundException extends LogicException {

  /**
   * 見つからなかったカタログ ID を指定して {@link CatalogNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param catalogId 見つからなかったカタログ ID 。
   */
  public CatalogNotFoundException(long catalogId) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND,
        new String[] {String.valueOf(catalogId)}, new String[] {String.valueOf(catalogId)});
  }

  /**
   * 見つからなかった複数のカタログ ID を指定して {@link CatalogNotFoundException} クラスのインスタンスを初期化します。
   * 
   * @param catalogIds 見つからなかった複数のカタログ ID 。
   */
  public CatalogNotFoundException(long... catalogIds) {
    super(null, ExceptionIdConstants.E_CATALOG_ID_NOT_FOUND,
        new String[] {joinCatalogIds(catalogIds)}, new String[] {joinCatalogIds(catalogIds)});
  }

  /**
   * 複数のカタログ ID をカンマ区切りの文字列に変換します。
   * 
   * @param catalogIds カタログ ID の配列。
   * @return カタログ ID をカンマ区切りの文字列に変換した結果。
   */
  private static String joinCatalogIds(long... catalogIds) {
    return String.join(", ",
        Arrays.stream(catalogIds).mapToObj(String::valueOf).toArray(String[]::new));
  }
}
