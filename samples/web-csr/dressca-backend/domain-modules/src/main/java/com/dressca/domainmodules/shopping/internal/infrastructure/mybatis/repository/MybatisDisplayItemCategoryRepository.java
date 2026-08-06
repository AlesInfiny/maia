package com.dressca.domainmodules.shopping.internal.infrastructure.mybatis.repository;

import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.domainmodules.common.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.domainmodules.common.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.domainmodules.shopping.displayitem.DisplayItemCategory;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemCategoryRepository;
import com.dressca.domainmodules.shopping.internal.infrastructure.mybatis.translator.ShoppingEntityTranslator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品カテゴリのリポジトリです。
 * 陳列品カテゴリ専用のテーブルは持たず、カタログカテゴリ（catalog_categories）を源泉として陳列品カテゴリを解決します。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemCategoryRepository implements DisplayItemCategoryRepository {

  private final CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<DisplayItemCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(ShoppingEntityTranslator::displayItemCategoryEntityTranslate)
        .collect(Collectors.toList());
  }

  @Override
  public DisplayItemCategory findById(UUID id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    return entity == null ? null
        : ShoppingEntityTranslator.displayItemCategoryEntityTranslate(entity);
  }
}
