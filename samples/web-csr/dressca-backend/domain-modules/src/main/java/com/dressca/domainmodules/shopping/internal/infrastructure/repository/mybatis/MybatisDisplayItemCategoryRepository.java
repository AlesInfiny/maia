package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemCategoryRepository;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.mapper.DisplayItemCategoryQueryMapper;
import com.dressca.domainmodules.shopping.model.DisplayItemCategory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品カテゴリのリポジトリです。
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
