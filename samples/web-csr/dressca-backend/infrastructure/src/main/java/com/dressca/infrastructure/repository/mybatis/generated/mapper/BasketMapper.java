package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BasketMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(BasketEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(BasketEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(BasketEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(BasketEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<BasketEntity> selectByExample(BasketEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasketEntity selectByPrimaryKey(Long id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") BasketEntity row, @Param("example") BasketEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") BasketEntity row, @Param("example") BasketEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(BasketEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(BasketEntity row);
}