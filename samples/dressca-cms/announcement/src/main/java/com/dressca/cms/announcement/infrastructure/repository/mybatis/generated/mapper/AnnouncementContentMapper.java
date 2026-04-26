package com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementContentMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    long countByExample(AnnouncementContentEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int deleteByExample(AnnouncementContentEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int deleteByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int insert(AnnouncementContentEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int insertSelective(AnnouncementContentEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    List<AnnouncementContentEntity> selectByExample(AnnouncementContentEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    AnnouncementContentEntity selectByPrimaryKey(UUID id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int updateByExampleSelective(@Param("row") AnnouncementContentEntity row, @Param("example") AnnouncementContentEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int updateByExample(@Param("row") AnnouncementContentEntity row, @Param("example") AnnouncementContentEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int updateByPrimaryKeySelective(AnnouncementContentEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: announcement_contents")
    int updateByPrimaryKey(AnnouncementContentEntity row);
}