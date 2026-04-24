package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.OrderEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    long countByExample(OrderEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int deleteByExample(OrderEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int insert(OrderEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int insertSelective(OrderEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    List<OrderEntity> selectByExample(OrderEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    OrderEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int updateByExampleSelective(@Param("row") OrderEntity row, @Param("example") OrderEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int updateByExample(@Param("row") OrderEntity row, @Param("example") OrderEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int updateByPrimaryKeySelective(OrderEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: orders")
    int updateByPrimaryKey(OrderEntity row);
}