package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogCategoryEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogCategoryMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(CatalogCategoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(CatalogCategoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(CatalogCategoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(CatalogCategoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<CatalogCategoryEntity> selectByExample(CatalogCategoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    CatalogCategoryEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") CatalogCategoryEntity row, @Param("example") CatalogCategoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") CatalogCategoryEntity row, @Param("example") CatalogCategoryEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(CatalogCategoryEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(CatalogCategoryEntity row);
}