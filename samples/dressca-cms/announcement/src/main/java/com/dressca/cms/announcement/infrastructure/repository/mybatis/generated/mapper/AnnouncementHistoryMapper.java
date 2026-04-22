package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementHistoryMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    long countByExample(AnnouncementHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int deleteByExample(AnnouncementHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int deleteByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int insert(AnnouncementHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int insertSelective(AnnouncementHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    List<AnnouncementHistoryEntity> selectByExample(AnnouncementHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    AnnouncementHistoryEntity selectByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int updateByExampleSelective(@Param("row") AnnouncementHistoryEntity row, @Param("example") AnnouncementHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int updateByExample(@Param("row") AnnouncementHistoryEntity row, @Param("example") AnnouncementHistoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int updateByPrimaryKeySelective(AnnouncementHistoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_history")
    int updateByPrimaryKey(AnnouncementHistoryEntity row);
}