package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogBrandMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    long countByExample(CatalogBrandEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int deleteByExample(CatalogBrandEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int insert(CatalogBrandEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int insertSelective(CatalogBrandEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    List<CatalogBrandEntity> selectByExample(CatalogBrandEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    CatalogBrandEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int updateByExampleSelective(@Param("row") CatalogBrandEntity row, @Param("example") CatalogBrandEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int updateByExample(@Param("row") CatalogBrandEntity row, @Param("example") CatalogBrandEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int updateByPrimaryKeySelective(CatalogBrandEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_brands")
    int updateByPrimaryKey(CatalogBrandEntity row);
}