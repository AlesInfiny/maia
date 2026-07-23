package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.display.DisplayCategory;
import com.dressca.applicationcore.display.DisplayCategoryRepository;
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
 * 陳列カテゴリのリポジトリです。
 * 陳列カテゴリ専用のテーブルは持たず、カタログカテゴリ（catalog_categories）を源泉として陳列カテゴリを解決します。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayCategoryRepository implements DisplayCategoryRepository {

  private final CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<DisplayCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(EntityTranslator::displayCategoryEntityTranslate).collect(Collectors.toList());
  }

  @Override
  public DisplayCategory findById(UUID id) {
    CatalogCategoryEntity entity = catalogCategoryMapper.selectByPrimaryKey(id);
    return entity == null ? null : EntityTranslator.displayCategoryEntityTranslate(entity);
  }
}
