package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementContentHistoryMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    long countByExample(AnnouncementContentHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int deleteByExample(AnnouncementContentHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int deleteByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int insert(AnnouncementContentHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int insertSelective(AnnouncementContentHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    List<AnnouncementContentHistoryEntity> selectByExample(AnnouncementContentHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    AnnouncementContentHistoryEntity selectByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int updateByExampleSelective(@Param("row") AnnouncementContentHistoryEntity row, @Param("example") AnnouncementContentHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int updateByExample(@Param("row") AnnouncementContentHistoryEntity row, @Param("example") AnnouncementContentHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int updateByPrimaryKeySelective(AnnouncementContentHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_content_history")
    int updateByPrimaryKey(AnnouncementContentHistoryEntity row);
}