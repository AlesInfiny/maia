package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BasketMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    long countByExample(BasketEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int deleteByExample(BasketEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int deleteByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int insert(BasketEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int insertSelective(BasketEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    List<BasketEntity> selectByExample(BasketEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    BasketEntity selectByPrimaryKey(Long id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int updateByExampleSelective(@Param("row") BasketEntity row, @Param("example") BasketEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int updateByExample(@Param("row") BasketEntity row, @Param("example") BasketEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int updateByPrimaryKeySelective(BasketEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: baskets")
    int updateByPrimaryKey(BasketEntity row);
}