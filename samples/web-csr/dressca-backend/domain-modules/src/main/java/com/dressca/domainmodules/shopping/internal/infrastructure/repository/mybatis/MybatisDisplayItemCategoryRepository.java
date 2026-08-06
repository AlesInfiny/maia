package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemCategoryRepository;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.mapper.DisplayItemCategoryQueryMapper;
import com.dressca.domainmodules.shopping.models.DisplayItemCategory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品カテゴリのリポジトリです。
 * 陳列品カテゴリ専用のテーブルは持たず、カタログカテゴリ（catalog_categories）を源泉として陳列品カテゴリを解決します。
 * カタログ管理コンテキストのテーブルは読み取り専用マッパー経由で参照し、更新は行いません。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemCategoryRepository implements DisplayItemCategoryRepository {

  private final DisplayItemCategoryQueryMapper mapper;

  @Override
  public List<DisplayItemCategory> getAll() {
    return mapper.selectAll();
  }

  @Override
  public DisplayItemCategory findById(UUID id) {
    return mapper.selectById(id);
  }
}
