package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.dressca.applicationcore.displayitem.DisplayBrand;
import com.dressca.applicationcore.displayitem.DisplayBrandRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogBrandMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import lombok.RequiredArgsConstructor;

/**
 * 掲載ブランドのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisDisplayBrandRepository implements DisplayBrandRepository {

  private final CatalogBrandMapper catalogBrandMapper;

  @Override
  public List<DisplayBrand> getAll() {
    CatalogBrandEntityExample example = new CatalogBrandEntityExample();
    return catalogBrandMapper.selectByExample(example).stream()
        .map(EntityTranslator::displayBrandEntityTranslate).collect(Collectors.toList());
  }

}
