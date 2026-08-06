package com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogBrandRepository;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.translator.CatalogEntityTranslator;
import com.dressca.domainmodules.catalogmanagement.models.CatalogBrand;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.mapper.CatalogBrandMapper;
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
        .map(CatalogEntityTranslator::catalogBrandEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public CatalogBrand findById(UUID id) {
    CatalogBrandEntity entity = catalogBrandMapper.selectByPrimaryKey(id);
    return entity == null ? null : CatalogEntityTranslator.catalogBrandEntityTranslate(entity);
  }
}
