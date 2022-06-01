package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.infrastructure.repository.mybatis.translater.EntityTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class MybatisCatalogCategoryRepository implements CatalogCategoryRepository {

  @Autowired
  private CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<CatalogCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(EntityTranslator::catalogCategoryEntityTranslate)
        .collect(Collectors.toList());
  }

}
