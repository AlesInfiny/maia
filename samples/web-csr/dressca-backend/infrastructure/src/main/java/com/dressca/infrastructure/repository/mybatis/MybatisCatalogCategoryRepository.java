package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
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
        .map(EntityTranslator::catalogCategoryEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public CatalogCategory findById(UUID id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    return entity == null ? null : EntityTranslator.catalogCategoryEntityTranslate(entity);
  }
}
