package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogBrandEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogBrandMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(CatalogBrandEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(CatalogBrandEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(CatalogBrandEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(CatalogBrandEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<CatalogBrandEntity> selectByExample(CatalogBrandEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    CatalogBrandEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") CatalogBrandEntity row, @Param("example") CatalogBrandEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") CatalogBrandEntity row, @Param("example") CatalogBrandEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(CatalogBrandEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(CatalogBrandEntity row);
}