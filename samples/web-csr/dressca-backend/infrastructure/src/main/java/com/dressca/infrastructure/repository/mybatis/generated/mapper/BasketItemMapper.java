package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BasketItemMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    long countByExample(BasketItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int deleteByExample(BasketItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int insert(BasketItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int insertSelective(BasketItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    List<BasketItemEntity> selectByExample(BasketItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    BasketItemEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int updateByExampleSelective(@Param("row") BasketItemEntity row, @Param("example") BasketItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int updateByExample(@Param("row") BasketItemEntity row, @Param("example") BasketItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int updateByPrimaryKeySelective(BasketItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: basket_items")
    int updateByPrimaryKey(BasketItemEntity row);
}