package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    long countByExample(AnnouncementEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int deleteByExample(AnnouncementEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int deleteByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int insert(AnnouncementEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int insertSelective(AnnouncementEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    List<AnnouncementEntity> selectByExample(AnnouncementEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    AnnouncementEntity selectByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int updateByExampleSelective(@Param("row") AnnouncementEntity row, @Param("example") AnnouncementEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int updateByExample(@Param("row") AnnouncementEntity row, @Param("example") AnnouncementEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int updateByPrimaryKeySelective(AnnouncementEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcements")
    int updateByPrimaryKey(AnnouncementEntity row);
}