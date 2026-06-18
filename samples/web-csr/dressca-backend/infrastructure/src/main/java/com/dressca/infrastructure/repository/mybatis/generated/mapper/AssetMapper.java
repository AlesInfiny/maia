package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(AssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(AssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(AssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AssetEntity> selectByExample(AssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AssetEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") AssetEntity row, @Param("example") AssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") AssetEntity row, @Param("example") AssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(AssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AssetEntity row);
}