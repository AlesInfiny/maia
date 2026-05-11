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
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(AnnouncementContentEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(AnnouncementContentEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AnnouncementContentEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(AnnouncementContentEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AnnouncementContentEntity> selectByExample(AnnouncementContentEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AnnouncementContentEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") AnnouncementContentEntity row, @Param("example") AnnouncementContentEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") AnnouncementContentEntity row, @Param("example") AnnouncementContentEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(AnnouncementContentEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AnnouncementContentEntity row);
}