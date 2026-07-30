package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.displayitem.DisplayItemBrand;
import com.dressca.applicationcore.displayitem.DisplayItemBrandRepository;
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
 * 陳列ブランドのリポジトリです。
 * 陳列ブランド専用のテーブルは持たず、カタログブランド（catalog_brands）を源泉として陳列ブランドを解決します。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemBrandRepository implements DisplayItemBrandRepository {

  private final CatalogBrandMapper catalogBrandMapper;

  @Override
  public List<DisplayItemBrand> getAll() {
    CatalogBrandEntityExample example = new CatalogBrandEntityExample();
    return catalogBrandMapper.selectByExample(example).stream()
        .map(EntityTranslator::displayItemBrandEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public DisplayItemBrand findById(UUID id) {
    CatalogBrandEntity entity = catalogBrandMapper.selectByPrimaryKey(id);
    return entity == null ? null : EntityTranslator.displayItemBrandEntityTranslate(entity);
  }
}
