package com.dressca.modules.catalog;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.modules.catalog.entity.CatalogCategoryEntity;
import com.dressca.modules.catalog.entity.CatalogCategoryEntityExample;
import com.dressca.modules.catalog.mapper.CatalogCategoryMapper;
import com.dressca.modules.common.translator.EntityTranslator;
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
