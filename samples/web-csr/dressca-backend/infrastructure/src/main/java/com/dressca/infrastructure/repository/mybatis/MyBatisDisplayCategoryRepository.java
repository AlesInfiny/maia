package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.dressca.applicationcore.displayitem.DisplayCategory;
import com.dressca.applicationcore.displayitem.DisplayCategoryRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogCategoryMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import lombok.RequiredArgsConstructor;

/**
 * 掲載カテゴリのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisDisplayCategoryRepository implements DisplayCategoryRepository {
  private final CatalogCategoryMapper catalogCategoryMapper;

  @Override
  public List<DisplayCategory> getAll() {
    CatalogCategoryEntityExample example = new CatalogCategoryEntityExample();
    return catalogCategoryMapper.selectByExample(example).stream()
        .map(EntityTranslator::displayCategoryEntityTranslate).collect(Collectors.toList());
  }
}
