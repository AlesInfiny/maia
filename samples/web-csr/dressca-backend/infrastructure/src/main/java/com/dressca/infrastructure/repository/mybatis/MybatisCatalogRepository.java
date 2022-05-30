package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class MybatisCatalogRepository implements CatalogRepository {

  @Autowired
  private JoinedCatalogItemMapper mapper;

  @Override
  public List<CatalogItem> findByCategoryIdIn(List<Long> categoryIds) {
    return mapper.findByCategoryIdIn(categoryIds);
  }

  @Override
  public List<CatalogItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page,
      int pageSize) {
    int offset = pageSize * page;
    return mapper.findByBrandIdAndCategoryId(brandId, categoryId, pageSize, offset);
  }

  @Override
  public List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds) {
    return mapper.findByCatalogItemIdIn(catalogItemIds);
  }

  @Override
  public int countByBrandIdAndCategoryId(long brandId, long categoryId) {
    return mapper.countByBrandIdAndCategoryId(brandId, categoryId);
  }

}
