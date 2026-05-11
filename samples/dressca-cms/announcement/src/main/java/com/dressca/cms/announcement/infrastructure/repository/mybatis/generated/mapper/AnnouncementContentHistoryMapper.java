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
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(AnnouncementContentHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(AnnouncementContentHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AnnouncementContentHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(AnnouncementContentHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AnnouncementContentHistoryEntity> selectByExample(AnnouncementContentHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AnnouncementContentHistoryEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") AnnouncementContentHistoryEntity row, @Param("example") AnnouncementContentHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") AnnouncementContentHistoryEntity row, @Param("example") AnnouncementContentHistoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(AnnouncementContentHistoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AnnouncementContentHistoryEntity row);
}