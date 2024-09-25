package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogItemMapper;
import com.dressca.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * カタログリポジトリ。
 */
@Repository
@AllArgsConstructor
public class MybatisCatalogRepository implements CatalogRepository {

  @Autowired
  private JoinedCatalogItemMapper mapper;

  @Autowired
  private CatalogItemMapper catalogItemMapper;

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

  @Override
  public List<CatalogItem> findWithPaging(int skipRows, int pageSize) {
    return mapper.findWithPaging(skipRows, pageSize);
  }

  @Override
  public Optional<CatalogItem> findById(long id) {
    CatalogItemEntity entity = catalogItemMapper.selectByPrimaryKey(id);
    CatalogItem item = EntityTranslator.catalogItemEntityTranslate(entity);
    return Optional.ofNullable(item);
  }

  @Override
  public CatalogItem add(CatalogItem item) {
    CatalogItemEntity entity = EntityTranslator.createCatalogItemEntity(item);
    catalogItemMapper.insert(entity);
    return item;
  }

  @Override
  public void remove(CatalogItem item) {
    CatalogItemEntityExample catalogItemExample = new CatalogItemEntityExample();
    catalogItemExample.createCriteria().andIdEqualTo(item.getId());
    catalogItemMapper.deleteByExample(catalogItemExample);
  }

  @Override
  public void update(CatalogItem item) {
    CatalogItemEntity entity = EntityTranslator.createCatalogItemEntity(item);
    catalogItemMapper.updateByPrimaryKey(entity);
  }
}
