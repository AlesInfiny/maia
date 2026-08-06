package com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogCategoryRepository;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.translator.CatalogEntityTranslator;
import com.dressca.domainmodules.catalogmanagement.models.CatalogCategory;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * カタログカテゴリのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisCatalogCategoryRepository implements CatalogCategoryRepository {

  private final CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<CatalogCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(CatalogEntityTranslator::catalogCategoryEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public CatalogCategory findById(UUID id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    return entity == null ? null : CatalogEntityTranslator.catalogCategoryEntityTranslate(entity);
  }
}
