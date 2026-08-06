package com.dressca.domainmodules.shopping.internal.infrastructure.mybatis.repository;

import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.domainmodules.common.mybatis.generated.mapper.CatalogBrandMapper;
import com.dressca.domainmodules.shopping.displayitem.DisplayItemBrand;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemBrandRepository;
import com.dressca.domainmodules.shopping.internal.infrastructure.mybatis.translator.ShoppingEntityTranslator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品ブランドのリポジトリです。
 * 陳列品ブランド専用のテーブルは持たず、カタログブランド（catalog_brands）を源泉として陳列品ブランドを解決します。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemBrandRepository implements DisplayItemBrandRepository {

  private final CatalogBrandMapper catalogBrandMapper;

  @Override
  public List<DisplayItemBrand> getAll() {
    CatalogBrandEntityExample example = new CatalogBrandEntityExample();
    return catalogBrandMapper.selectByExample(example).stream()
        .map(ShoppingEntityTranslator::displayItemBrandEntityTranslate)
        .collect(Collectors.toList());
  }

  @Override
  public DisplayItemBrand findById(UUID id) {
    CatalogBrandEntity entity = catalogBrandMapper.selectByPrimaryKey(id);
    return entity == null ? null : ShoppingEntityTranslator.displayItemBrandEntityTranslate(entity);
  }
}
