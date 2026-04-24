package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderItemEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    long countByExample(OrderItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int deleteByExample(OrderItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int insert(OrderItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int insertSelective(OrderItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    List<OrderItemEntity> selectByExample(OrderItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    OrderItemEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int updateByExampleSelective(@Param("row") OrderItemEntity row, @Param("example") OrderItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int updateByExample(@Param("row") OrderItemEntity row, @Param("example") OrderItemEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int updateByPrimaryKeySelective(OrderItemEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: order_items")
    int updateByPrimaryKey(OrderItemEntity row);
}