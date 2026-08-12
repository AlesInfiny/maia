package com.dressca.boundedcontexts.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.boundedcontexts.shopping.internal.domain.repository.DisplayItemBrandRepository;
import com.dressca.boundedcontexts.shopping.internal.infrastructure.repository.mybatis.mapper.DisplayItemBrandQueryMapper;
import com.dressca.boundedcontexts.shopping.model.DisplayItemBrand;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品ブランドのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemBrandRepository implements DisplayItemBrandRepository {

  private final DisplayItemBrandQueryMapper mapper;

  @Override
  public List<DisplayItemBrand> getAll() {
    return mapper.selectAll();
  }

  @Override
  public DisplayItemBrand findById(UUID id) {
    return mapper.selectById(id);
  }
}
