package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(OrderEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(OrderEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(OrderEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(OrderEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<OrderEntity> selectByExample(OrderEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    OrderEntity selectByPrimaryKey(UUID id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") OrderEntity row, @Param("example") OrderEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") OrderEntity row, @Param("example") OrderEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(OrderEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(OrderEntity row);
}