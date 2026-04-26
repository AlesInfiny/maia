package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogItemAssetMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    long countByExample(CatalogItemAssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int deleteByExample(CatalogItemAssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int insert(CatalogItemAssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int insertSelective(CatalogItemAssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    List<CatalogItemAssetEntity> selectByExample(CatalogItemAssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    CatalogItemAssetEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int updateByExampleSelective(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int updateByExample(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int updateByPrimaryKeySelective(CatalogItemAssetEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_item_assets")
    int updateByPrimaryKey(CatalogItemAssetEntity row);
}