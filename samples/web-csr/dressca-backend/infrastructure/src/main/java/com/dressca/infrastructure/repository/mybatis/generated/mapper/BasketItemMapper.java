package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BasketItemMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(BasketItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(BasketItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(BasketItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(BasketItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<BasketItemEntity> selectByExample(BasketItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasketItemEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") BasketItemEntity row, @Param("example") BasketItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") BasketItemEntity row, @Param("example") BasketItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(BasketItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(BasketItemEntity row);
}