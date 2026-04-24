package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogCategoryMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    long countByExample(CatalogCategoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int deleteByExample(CatalogCategoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int insert(CatalogCategoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int insertSelective(CatalogCategoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    List<CatalogCategoryEntity> selectByExample(CatalogCategoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    CatalogCategoryEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int updateByExampleSelective(@Param("row") CatalogCategoryEntity row, @Param("example") CatalogCategoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int updateByExample(@Param("row") CatalogCategoryEntity row, @Param("example") CatalogCategoryEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int updateByPrimaryKeySelective(CatalogCategoryEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: catalog_categories")
    int updateByPrimaryKey(CatalogCategoryEntity row);
}