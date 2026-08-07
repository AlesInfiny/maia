package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemBrandRepository;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.mapper.DisplayItemBrandQueryMapper;
import com.dressca.domainmodules.shopping.model.DisplayItemBrand;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品ブランドのリポジトリです。
 * 陳列品ブランド専用のテーブルは持たず、カタログブランド（catalog_brands）を源泉として陳列品ブランドを解決します。
 * カタログ管理コンテキストのテーブルは読み取り専用マッパー経由で参照し、更新は行いません。
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
