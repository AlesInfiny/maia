package com.dressca.applicationmodules.shopping.internal.infrastructure.repository.mybatis;

import com.dressca.applicationmodules.shopping.entity.DisplayItem;
import com.dressca.applicationmodules.shopping.internal.domain.repository.DisplayItemRepository;
import com.dressca.applicationmodules.shopping.internal.infrastructure.repository.mybatis.mapper.JoinedDisplayItemMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 陳列品のリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisDisplayItemRepository implements DisplayItemRepository {

  private final JoinedDisplayItemMapper mapper;

  @Override
  public List<DisplayItem> findByBrandIdAndCategoryId(UUID brandId, UUID categoryId, int page,
      int pageSize) {
    int offset = pageSize * (page - 1);
    return mapper.findByBrandIdAndCategoryId(brandId, categoryId, pageSize, offset);
  }

  @Override
  public int countByBrandIdAndCategoryId(UUID brandId, UUID categoryId) {
    return mapper.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  @Override
  public List<DisplayItem> findByDisplayItemIdIn(List<UUID> displayItemIds) {
    return mapper.findByDisplayItemIdIn(displayItemIds);
  }

  @Override
  public List<DisplayItem> findByDisplayItemIdInIncludingDeleted(List<UUID> displayItemIds) {
    return mapper.findByDisplayItemIdInIncludingDeleted(displayItemIds);
  }

  @Override
  public List<DisplayItem> findDeletedItemsByDisplayItemIdIn(List<UUID> displayItemIds) {
    return mapper.findDeletedItemsByDisplayItemIdIn(displayItemIds);
  }
}
