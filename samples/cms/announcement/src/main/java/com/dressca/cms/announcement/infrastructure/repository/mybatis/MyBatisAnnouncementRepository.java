package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntityExample;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementMapper;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper.JoinedAnnouncementMapper;
import lombok.AllArgsConstructor;

/**
 * お知らせメッセージのリポジトリの実装クラスです。
 */
@Repository
@AllArgsConstructor
public class MyBatisAnnouncementRepository implements AnnouncementRepository {

  private final AnnouncementMapper mapper;
  private final JoinedAnnouncementMapper joinedMapper;

  @Override
  public long countByIsDeletedFalse() {
    AnnouncementEntityExample example = new AnnouncementEntityExample();
    example.createCriteria().andIsDeletedEqualTo(false);
    return mapper.countByExample(example);
  }

  @Override
  public List<Announcement> findByPageNumberAndPageSize(int pageNumber, int pageSize) {
    int offset = pageSize * (pageNumber - 1);
    return joinedMapper.findByPageNumberAndPageSize(pageSize, offset);
  }
}
