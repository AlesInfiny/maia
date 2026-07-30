package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.displayitem.DisplayItemCategory;
import com.dressca.applicationcore.displayitem.DisplayItemCategoryRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
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
        .map(EntityTranslator::displayItemCategoryEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public DisplayItemCategory findById(UUID id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    return entity == null ? null : EntityTranslator.displayItemCategoryEntityTranslate(entity);
  }
}
