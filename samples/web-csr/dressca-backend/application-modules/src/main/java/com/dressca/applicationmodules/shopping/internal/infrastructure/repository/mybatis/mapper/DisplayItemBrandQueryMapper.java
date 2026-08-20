package com.dressca.applicationmodules.shopping.internal.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationmodules.shopping.entity.DisplayItemBrand;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 陳列品ブランドを取得するための読み取り専用マッパーのインターフェースです。
 *
 * <p>陳列品ブランド専用のテーブルは持たず、カタログ管理コンテキストが所有する
 * カタログブランド（catalog_brands）を元に陳列品ブランドを解決します。</p>
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
