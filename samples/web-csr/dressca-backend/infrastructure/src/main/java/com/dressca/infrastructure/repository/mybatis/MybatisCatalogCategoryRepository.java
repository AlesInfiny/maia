package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

/**
 * カタログカテゴリのリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MybatisCatalogCategoryRepository implements CatalogCategoryRepository {

  @Autowired
  private CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<CatalogCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(EntityTranslator::catalogCategoryEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public CatalogCategory findById(long id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    CatalogCategory category = EntityTranslator.catalogCategoryEntityTranslate(entity);
    return category;
  }
}
