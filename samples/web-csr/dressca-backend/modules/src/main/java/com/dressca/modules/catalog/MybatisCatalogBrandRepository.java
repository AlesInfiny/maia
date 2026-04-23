package com.dressca.modules.catalog;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.modules.catalog.entity.CatalogBrandEntity;
import com.dressca.modules.catalog.entity.CatalogBrandEntityExample;
import com.dressca.modules.catalog.mapper.CatalogBrandMapper;
import com.dressca.modules.common.translator.EntityTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

/**
 * カタログブランドのリポジトリです。
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
        .map(EntityTranslator::catalogBrandEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public CatalogBrand findById(long id) {
    CatalogBrandEntity entity = catalogBrandMapper.selectByPrimaryKey(id);
    CatalogBrand brand = EntityTranslator.catalogBrandEntityTranslate(entity);
    return brand;
  }
}
