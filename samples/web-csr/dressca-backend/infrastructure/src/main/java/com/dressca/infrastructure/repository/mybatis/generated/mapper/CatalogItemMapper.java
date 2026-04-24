package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogItemMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    long countByExample(CatalogItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int deleteByExample(CatalogItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int insert(CatalogItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int insertSelective(CatalogItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    List<CatalogItemEntity> selectByExample(CatalogItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    CatalogItemEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int updateByExampleSelective(@Param("row") CatalogItemEntity row, @Param("example") CatalogItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int updateByExample(@Param("row") CatalogItemEntity row, @Param("example") CatalogItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int updateByPrimaryKeySelective(CatalogItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_items")
    int updateByPrimaryKey(CatalogItemEntity row);
}