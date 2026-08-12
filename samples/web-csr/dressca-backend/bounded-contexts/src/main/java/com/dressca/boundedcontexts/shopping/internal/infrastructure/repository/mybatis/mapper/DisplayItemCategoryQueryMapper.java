package com.dressca.boundedcontexts.shopping.internal.infrastructure.repository.mybatis.mapper;

import com.dressca.boundedcontexts.shopping.model.DisplayItemCategory;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 陳列品カテゴリを取得するための読み取り専用マッパーのインターフェースです。
 *
 * <p>陳列品カテゴリ専用のテーブルは持たず、カタログ管理コンテキストが所有する
 * カタログカテゴリ（catalog_categories）を元に陳列品カテゴリを解決します。</p>
 */
@Mapper
public interface DisplayItemCategoryQueryMapper {

  /**
   * 陳列品カテゴリをすべて取得します。
   *
   * @return 陳列品カテゴリのリスト。
   */
  List<DisplayItemCategory> selectAll();

  /**
   * ID を指定して陳列品カテゴリを取得します。
   *
   * @param id 陳列品カテゴリ ID 。
   * @return 陳列品カテゴリ。存在しない場合は null 。
   */
  DisplayItemCategory selectById(@Param("id") UUID id);
}
