package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.mapper;

import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.dressca.domainmodules.shopping.model.DisplayItemBrand;

/**
 * 陳列品ブランドを取得するための読み取り専用マッパーのインターフェースです。
 *
 * <p>陳列品ブランド専用のテーブルは持たず、カタログ管理コンテキストが所有する
 * カタログブランド（catalog_brands）を源泉として陳列品ブランドを解決します。
 * 買い物コンテキストはカタログ管理コンテキストの下流にあたるため、
 * 参照のみを行い、更新は一切行いません。</p>
 *
 * <p>カタログ管理コンテキストの生成マッパーを共有せず、買い物コンテキストが必要とする
 * 列だけを取得する SELECT をここに閉じ込めることで、カタログ側のスキーマに対する
 * 依存範囲を明示しています。</p>
 */
@Mapper
public interface DisplayItemBrandQueryMapper {

  /**
   * 陳列品ブランドをすべて取得します。
   *
   * @return 陳列品ブランドのリスト。
   */
  List<DisplayItemBrand> selectAll();

  /**
   * ID を指定して陳列品ブランドを取得します。
   *
   * @param id 陳列品ブランド ID 。
   * @return 陳列品ブランド。存在しない場合は null 。
   */
  DisplayItemBrand selectById(@Param("id") UUID id);
}
