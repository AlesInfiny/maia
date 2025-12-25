package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.CatalogItemMapper;
import com.dressca.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * カタログのリポジトリです。
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
    int offset = pageSize * (page - 1);
    return mapper.findByBrandIdAndCategoryId(brandId, categoryId, pageSize, offset);
  }

  @Override
  public List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(long brandId, long categoryId,
      int page, int pageSize) {
    int offset = pageSize * (page - 1);
    return mapper.findByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId, pageSize, offset);
  }

  @Override
  public List<CatalogItem> findByCatalogItemIdIn(List<Long> catalogItemIds) {
    return mapper.findByCatalogItemIdIn(catalogItemIds);
  }

  @Override
  public List<CatalogItem> findByCatalogItemIdInIncludingDeleted(List<Long> catalogItemIds) {
    return mapper.findByCatalogItemIdInIncludingDeleted(catalogItemIds);
  }

  @Override
  public int countByBrandIdAndCategoryId(long brandId, long categoryId) {
    return mapper.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  @Override
  public int countByBrandIdAndCategoryIdIncludingDeleted(long brandId, long categoryId) {
    return mapper.countByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId);
  }

  @Override
  public List<CatalogItem> findWithPaging(int skipRows, int pageSize) {
    return mapper.findWithPaging(skipRows, pageSize);
  }

  @Override
  public CatalogItem findById(long id) {
    return mapper.findById(id);
  }

  @Override
  public CatalogItem findByIdIncludingDeleted(long id) {
    return mapper.findByIdIncludingDeleted(id);
  }

  @Override
  public CatalogItem add(CatalogItem item) {
    CatalogItemEntity entity = EntityTranslator.createCatalogItemEntity(item);
    catalogItemMapper.insert(entity);
    item.setId(entity.getId());
    return item;
  }

  @Override
  public int remove(Long id, OffsetDateTime rowVersion) {
    CatalogItemEntityExample catalogItemExample = new CatalogItemEntityExample();
    catalogItemExample.createCriteria().andIdEqualTo(id).andRowVersionEqualTo(rowVersion);
    return catalogItemMapper.deleteByExample(catalogItemExample);
  }

  @Override
  public int update(CatalogItem item) {
    CatalogItemEntity entity = EntityTranslator.createCatalogItemEntity(item);
    return this.catalogItemMapper.updateByPrimaryKey(entity);
  }
}
