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
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(AnnouncementEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(AnnouncementEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AnnouncementEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(AnnouncementEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AnnouncementEntity> selectByExample(AnnouncementEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AnnouncementEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") AnnouncementEntity row, @Param("example") AnnouncementEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") AnnouncementEntity row, @Param("example") AnnouncementEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(AnnouncementEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AnnouncementEntity row);
}