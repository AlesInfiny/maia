package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogBrandMapper;
import com.dressca.infrastructure.repository.mybatis.translater.EntityTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

/**
 * カタログブランドリポジトリ。
 */
@Repository
@AllArgsConstructor
public class MybatisCatalogBrandRepository implements CatalogBrandRepository {

  @Autowired
  CatalogBrandMapper catalogBrandMapper;

  @Override
  public List<CatalogBrand> getAll() {
    CatalogBrandEntityExample example = new CatalogBrandEntityExample();
    return catalogBrandMapper.selectByExample(example).stream()
        .map(EntityTranslator::catalogBrandEntityTranslate)
        .collect(Collectors.toList());
  }
}
