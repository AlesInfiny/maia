package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(OrderItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(OrderItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(OrderItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(OrderItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<OrderItemEntity> selectByExample(OrderItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    OrderItemEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") OrderItemEntity row, @Param("example") OrderItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") OrderItemEntity row, @Param("example") OrderItemEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(OrderItemEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(OrderItemEntity row);
}