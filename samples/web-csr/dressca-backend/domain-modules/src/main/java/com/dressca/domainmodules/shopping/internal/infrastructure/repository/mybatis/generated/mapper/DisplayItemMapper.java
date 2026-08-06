package com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.DisplayItemEntity;
import com.dressca.domainmodules.shopping.internal.infrastructure.repository.mybatis.generated.entity.DisplayItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DisplayItemMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(DisplayItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(DisplayItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(DisplayItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(DisplayItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<DisplayItemEntity> selectByExample(DisplayItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    DisplayItemEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") DisplayItemEntity row, @Param("example") DisplayItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") DisplayItemEntity row, @Param("example") DisplayItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(DisplayItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(DisplayItemEntity row);
}