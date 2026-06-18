package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CatalogItemMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(CatalogItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(CatalogItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(CatalogItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(CatalogItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<CatalogItemEntity> selectByExample(CatalogItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    CatalogItemEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") CatalogItemEntity row, @Param("example") CatalogItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") CatalogItemEntity row, @Param("example") CatalogItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(CatalogItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(CatalogItemEntity row);
}