package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogItemAssetMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(CatalogItemAssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(CatalogItemAssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(CatalogItemAssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(CatalogItemAssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<CatalogItemAssetEntity> selectByExample(CatalogItemAssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    CatalogItemAssetEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(CatalogItemAssetEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(CatalogItemAssetEntity row);
}