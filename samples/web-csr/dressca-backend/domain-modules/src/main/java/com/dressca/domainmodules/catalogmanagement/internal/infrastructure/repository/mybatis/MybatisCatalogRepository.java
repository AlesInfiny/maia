package com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis;

import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogRepository;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.mapper.JoinedCatalogItemMapper;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.translator.CatalogEntityTranslator;
import com.dressca.domainmodules.catalogmanagement.models.CatalogItem;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.entity.CatalogItemEntityExample;
import com.dressca.domainmodules.catalogmanagement.internal.infrastructure.repository.mybatis.generated.mapper.CatalogItemMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * カタログのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisCatalogRepository implements CatalogRepository {

  private final JoinedCatalogItemMapper mapper;
  private final CatalogItemMapper catalogItemMapper;

  @Override
  public List<CatalogItem> findByCategoryIdIn(List<UUID> categoryIds) {
    return mapper.findByCategoryIdIn(categoryIds);
  }

  @Override
  public List<CatalogItem> findByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId,
      int page, int pageSize) {
    int offset = pageSize * (page - 1);
    return mapper.findByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId, pageSize, offset);
  }

  @Override
  public int countByBrandIdAndCategoryIdIncludingDeleted(UUID brandId, UUID categoryId) {
    return mapper.countByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId);
  }

  @Override
  public List<CatalogItem> findWithPaging(int skipRows, int pageSize) {
    return mapper.findWithPaging(skipRows, pageSize);
  }

  @Override
  public CatalogItem findById(UUID id) {
    return mapper.findById(id);
  }

  @Override
  public CatalogItem findByIdIncludingDeleted(UUID id) {
    return mapper.findByIdIncludingDeleted(id);
  }

  @Override
  public CatalogItem add(CatalogItem item) {
    CatalogItemEntity entity = CatalogEntityTranslator.createCatalogItemEntity(item);
    catalogItemMapper.insert(entity);
    return item;
  }

  @Override
  public int remove(UUID id, OffsetDateTime rowVersion) {
    CatalogItemEntityExample catalogItemExample = new CatalogItemEntityExample();
    catalogItemExample.createCriteria().andIdEqualTo(id).andRowVersionEqualTo(rowVersion);
    return catalogItemMapper.deleteByExample(catalogItemExample);
  }

  @Override
  public int update(CatalogItem item) {
    CatalogItemEntity entity = CatalogEntityTranslator.createCatalogItemEntity(item);
    return this.catalogItemMapper.updateByPrimaryKey(entity);
  }
}
