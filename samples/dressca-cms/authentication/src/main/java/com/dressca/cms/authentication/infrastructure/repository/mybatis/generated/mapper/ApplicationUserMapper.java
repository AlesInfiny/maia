package com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntity;
import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplicationUserMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    long countByExample(ApplicationUserEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByExample(ApplicationUserEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(Integer id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(ApplicationUserEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insertSelective(ApplicationUserEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<ApplicationUserEntity> selectByExample(ApplicationUserEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    ApplicationUserEntity selectByPrimaryKey(Integer id);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExampleSelective(@Param("row") ApplicationUserEntity row, @Param("example") ApplicationUserEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByExample(@Param("row") ApplicationUserEntity row, @Param("example") ApplicationUserEntityExample example);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKeySelective(ApplicationUserEntity row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(ApplicationUserEntity row);
}