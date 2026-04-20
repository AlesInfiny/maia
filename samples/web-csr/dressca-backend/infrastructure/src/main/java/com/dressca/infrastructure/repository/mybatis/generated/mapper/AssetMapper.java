package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.AssetEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    long countByExample(AssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int deleteByExample(AssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int insert(AssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int insertSelective(AssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    List<AssetEntity> selectByExample(AssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    AssetEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int updateByExampleSelective(@Param("row") AssetEntity row, @Param("example") AssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int updateByExample(@Param("row") AssetEntity row, @Param("example") AssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int updateByPrimaryKeySelective(AssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: assets")
    int updateByPrimaryKey(AssetEntity row);
}