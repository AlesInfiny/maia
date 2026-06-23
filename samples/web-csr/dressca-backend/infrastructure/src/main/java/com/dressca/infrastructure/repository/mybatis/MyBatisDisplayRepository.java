package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayRepository;
import com.dressca.infrastructure.repository.mybatis.mapper.JoinedDisplayItemMapper;
import lombok.RequiredArgsConstructor;

/**
 * 掲載品のリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisDisplayRepository implements DisplayRepository {

  private final JoinedDisplayItemMapper mapper;

  @Override
  public int countByBrandIdAndCategoryId(long brandId, long categoryId) {
    return mapper.countByBrandIdAndCategoryId(brandId, categoryId);
  }

  @Override
  public List<DisplayItem> findByBrandIdAndCategoryId(long brandId, long categoryId, int page,
      int pageSize) {
    int offset = pageSize * (page - 1);
    return mapper.findByBrandIdAndCategoryId(brandId, categoryId, pageSize, offset);
  }

  @Override
  public List<DisplayItem> findByDisplayItemIdInIncludingDeleted(List<Long> displayItemIds) {
    return mapper.findByDisplayItemIdInIncludingDeleted(displayItemIds);
  }

  @Override
  public DisplayItem findByIdIncludingDeleted(long id) {
    return mapper.findByIdIncludingDeleted(id);
  }

  @Override
  public List<DisplayItem> findDeletedItemsByDisplayItemIdIn(List<Long> displayItemIds) {
    return mapper.findDeletedItemsByDisplayItemIdIn(displayItemIds);
  }

  @Override
  public List<DisplayItem> findByDisplayItemIdIn(List<Long> displayItemIds) {
    return mapper.findByDisplayItemIdIn(displayItemIds);
  }

}
