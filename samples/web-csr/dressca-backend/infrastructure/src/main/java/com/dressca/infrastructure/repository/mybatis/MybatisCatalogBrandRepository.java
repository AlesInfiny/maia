package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogBrandMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

/**
 * カタログブランドのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisCatalogBrandRepository implements CatalogBrandRepository {

  private final CatalogBrandMapper catalogBrandMapper;

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
