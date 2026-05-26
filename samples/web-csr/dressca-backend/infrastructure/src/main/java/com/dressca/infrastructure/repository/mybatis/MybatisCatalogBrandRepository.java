package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogBrandMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
  public CatalogBrand findById(UUID id) {
    CatalogBrandEntity entity = catalogBrandMapper.selectByPrimaryKey(id);
    return entity == null ? null : EntityTranslator.catalogBrandEntityTranslate(entity);
  }
}
