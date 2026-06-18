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
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(AnnouncementHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(AnnouncementHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AnnouncementHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(AnnouncementHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AnnouncementHistoryEntity> selectByExample(AnnouncementHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AnnouncementHistoryEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") AnnouncementHistoryEntity row, @Param("example") AnnouncementHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") AnnouncementHistoryEntity row, @Param("example") AnnouncementHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(AnnouncementHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AnnouncementHistoryEntity row);
}